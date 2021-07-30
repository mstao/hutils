package me.mingshan.util.cache.extend.annotation;

import java.lang.annotation.*;

/**
 * 该注解只适用于批量删除同一类缓存key的情况，即key的前缀一直，后半部分参数不一致，想把其删除。<br/>
 * 如果需要删除多个没有关系的key，可以使用 Spring的{@code @Caching(evict={})} 相关注解。
 * <br/>
 * 使用示例 ：
 * <br/>
 * {@code
 *     @CacheEvictList(value = RedisKeyConstants.WMS_USER_MENU_LIST_KEY_PREFIX, key = "#ids")
 *     public void batchDelete(List<Integer> ids) {
 *         // do
 *     }
 * }
 *
 *
 * @author hanjuntao
 * @date 2021/7/30
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheEvictList {
    /** 完整key的value，相当key的前缀 */
    String value() default "";

    /** key的具体参数，支持SPEL表达式 */
    String key() default "";
}
