package me.mingshan.util.orm.extend.optimisticlocker;

import me.mingshan.util.StringUtil;
import me.mingshan.util.orm.entity.VersionPEntity;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 版本号控制拦截器
 *
 * <br/>
 * 使用方式：
 *
 * {@code
 *  @Configuration
 *  public class MyBatisConfig {
 *
 *     @Bean
 *     public OptimisticLockerInterceptor optimisticLockerInterceptor(){
 *         return new OptimisticLockerInterceptor();
 *     }
 *  }
 * }
 *
 * <p>
 * DB层仅支持以下方法：
 * <ul>
 *    <li>1. updateByPrimaryKey</li>
 *    <li>2. delete</li>
 *    <li>3. updateByPrimaryKeySelective</li>
 *    <li>4. updateByExample</li>
 *    <li>5. updateByExampleSelective</li>
 * </ul>
 * <p>
 *
 * 注意点：<br/>
 * 数据库持久化对象需要继承{@link VersionPEntity}
 *
 * 参考：<br/>
 * https://www.jianshu.com/p/0a72bb1f6a21
 *
 * @author hanjuntao
 * @date 2021/8/2
 */
@Intercepts({@Signature(
            type = Executor.class,
            method = "update", // update 包括了最常用的 insert/update/delete 三种操作
            args = {MappedStatement.class, Object.class})})
public class OptimisticLockerInterceptor implements Interceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(OptimisticLockerInterceptor.class);
    /**
     * 支持拦截的方法
     */
    private static final Map<String, String> SUPPORT_METHODS_MAP = new HashMap<>();

    /*
     * 初始化
     */
    static {
        SUPPORT_METHODS_MAP.put("updateByPrimaryKey", null);
        SUPPORT_METHODS_MAP.put("delete", null);
        SUPPORT_METHODS_MAP.put("updateByPrimaryKeySelective", null);
        SUPPORT_METHODS_MAP.put("updateByExample", null);
        SUPPORT_METHODS_MAP.put("updateByExampleSelective", null);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        LOGGER.info("乐观锁：执行intercept方法：{}", invocation.toString());

        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];

        // 必须是继承自VersionPEntity才可以使用版本号
        if (!(parameterObject instanceof VersionPEntity)) {
            return invocation.proceed();
        }

        // id为执行的mapper方法的全路径名，如com.mapper.UserMapper.updateByPrimaryKey
        String id = ms.getId();
        LOGGER.info("乐观锁，ID: {}", id);

        // 仅支持拦截特定的方法
        String[] targetMethodArr = id.split("\\.");
        String method = targetMethodArr[targetMethodArr.length - 1];
        boolean contains = SUPPORT_METHODS_MAP.containsKey(method);
        if (!contains) {
            return invocation.proceed();
        }

        // sql语句类型 select、delete、insert、update
        String sqlCommandType = ms.getSqlCommandType().toString();
        LOGGER.info("乐观锁，sqlCommandType: {}", sqlCommandType);

        BoundSql boundSql = ms.getBoundSql(parameterObject);
        String origSql = boundSql.getSql();
        LOGGER.info("乐观锁，原始SQL: {}", origSql);

        Long version = ((VersionPEntity) parameterObject).getVersion();

        // 改写SQL
        String newSql = rewriteSql(origSql, version);
        if (origSql.equals(newSql)) {
            return invocation.proceed();
        }

        // 重新new一个查询语句对象
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), newSql,
                boundSql.getParameterMappings(), boundSql.getParameterObject());

        // 把新的查询放到statement里
        MappedStatement newMs = newMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }

        Object[] queryArgs = invocation.getArgs();
        queryArgs[0] = newMs;

        LOGGER.info("乐观锁，改写的SQL: {}", newSql);

        return invocation.proceed();
    }

    /**
     * 支持重新 update 语句和 delete 语句
     * <p>
     * 注意两者原始语句必须有 where ，没有where条件不处理，存在in的，也不处理
     * <p>
     * 场景1：
     *
     * 原始SQL:     update tableName set name = 'AA', code = 'zz' where id = 1;
     * 改写后的SQL:  update tableName set name = 'AA', code = 'zz', version = version + 1 where id = 1 and version = oldVersion;
     * <p>
     *
     * 场景2：
     *
     * 原始SQL:     delete tableName where id = 1;
     * 改写后的SQL:  delete tableName where id = 1 and version = oldVersion;
     *
     * @param origSql 原始的sql
     * @param version 当前版本号
     * @return 改写后的版本号
     */
    private static String rewriteSql(String origSql, Long version) {
        if (StringUtil.isEmpty(origSql) || version == null) {
            return origSql;
        }

        String lowCaseOrigSql = origSql.toLowerCase();

        // 判断sql是否有where条件
        boolean existWhere = lowCaseOrigSql.contains("where");
        if (!existWhere) {
            return origSql;
        }

        // 判断是 更新还是删除
        if (lowCaseOrigSql.startsWith("update")) {
            String[] sqlArr = origSql.split("(?i)where");

            String s1 = sqlArr[0];
            String s2 = sqlArr[1];

            s1 += " , version = version + 1 ";
            s2 += " and version = " + version;

            return s1 + " where " + s2;
        }
        if (lowCaseOrigSql.startsWith("delete")) {
            origSql += " and version = " + version;

            return origSql;
        }

        return origSql;
    }

    /**
     * 定义一个内部辅助类，作用是包装 SQL
     */
    static class BoundSqlSqlSource implements SqlSource {
        private final BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }

    }

    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new
                MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    @Override
    public Object plugin(Object target) {
        LOGGER.info("plugin方法：{}", target);

        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        // 获取属性
        // String value1 = properties.getProperty("prop1");
        LOGGER.info("properties方法：{}", properties.toString());
    }
}
