package me.mingshan.util.cache.extend.aspect;

import lombok.extern.slf4j.Slf4j;
import me.mingshan.util.StringUtil;
import me.mingshan.util.cache.extend.annotation.CacheEvictList;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 批量删除缓存切面
 *
 * @author hanjuntao
 * @date 2021/7/30
 */
@Aspect
@Slf4j
public class CacheEvictListAspect {
    private static final String SEPARATOR = "::";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @AfterReturning("@annotation(me.mingshan.util.cache.extend.annotation.CacheEvictList)")
    public void remove(JoinPoint point) throws NoSuchMethodException {
        Signature signature = point.getSignature();
        // 这个方法是代理对象上的
        Method method = ((MethodSignature) signature).getMethod();

        // 这个方法才是目标对象上有注解的方法
        Method realMethod = point.getTarget().getClass().getDeclaredMethod(signature.getName(),
                method.getParameterTypes());

        CacheEvictList cacheEvictList = realMethod.getAnnotation(CacheEvictList.class);
        if (cacheEvictList == null) {
            return;
        }

        String value = cacheEvictList.value();
        String key = cacheEvictList.key();
        if (StringUtil.isEmpty(key)) {
            return;
        }

        String keyPrefix = "";

        if (!StringUtil.isEmpty(value)) {
            keyPrefix = keyPrefix + value + SEPARATOR;
        }

        List<String> allKey = new ArrayList<>();

        List parsedList;
        if (key.contains("#")) {
            parsedList = parseKey(key, method, point.getArgs());
            if (CollectionUtils.isEmpty(parsedList)) {
                return;
            }

            String finalKeyPrefix = keyPrefix;
            parsedList.forEach(item -> allKey.add(finalKeyPrefix + item));
        } else {
            allKey.add(keyPrefix + key);
        }

        stringRedisTemplate.delete(allKey);
        log.info("Cache key: {} deleted", allKey);
    }

    /**
     * parseKey from SPEL
     */
    private static List parseKey(String key, Method method, Object[] args) {
        LocalVariableTableParameterNameDiscoverer u =
                new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);
        if (paraNameArr == null || paraNameArr.length == 0) {
            return Collections.emptyList();
        }

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, List.class);
    }
}
