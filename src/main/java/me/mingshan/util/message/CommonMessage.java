package me.mingshan.util.message;

import io.swagger.annotations.ApiModel;
import me.mingshan.util.AnnotationUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 公共提示信息
 *
 * @author hanjuntao walkerhan@126.com
 * @date 2021/7/12
 */
public class CommonMessage {
    /** 实体model与自定名称缓存：key: class.getName; value: modelName */
    private static final Map<String, String> ENTITY_MODEL_NAME_MAP = new ConcurrentHashMap<>();

    /**
     * xx不能为空
     */
    private static final String MESSAGE_NOT_NULL = "%s不能为空";

    /**
     * xx: yy不存在
     */
    private static final String MESSAGE_NOT_EXIST = "%s: %s不存在";

    /**
     * xx不存在 - 没有参数
     */
    private static final String MESSAGE_NOT_EXIST_NO_PARAM = "%s不存在";

    /**
     * xx不能为空
     *
     * @param entity 实体信息
     * @return 格式化后的提示
     */
    public static String notNull(String entity) {
        return String.format(MESSAGE_NOT_NULL, entity);
    }

    /**
     * xx不能为空
     *
     * @param entity 实体信息, 必须配置{@link ApiModel}中的value属性
     * @return 格式化后的提示
     */
    public static String notNull(Class<?> entity) {
        return notNull(getModelValue(entity));
    }

    /**
     * xx: yy不存在<br/>
     * <p>
     * 例如：站点：秦林路站 不存在
     *
     * @param entity 实体信息
     * @param arg    实体参数
     * @return 格式化后的提示
     */
    public static String notExist(String entity, String arg) {
        return String.format(MESSAGE_NOT_EXIST, entity, arg);
    }

    /**
     * xx 不存在<br/>
     * <p>
     * 例如：站点 不存在
     *
     * @param entity 实体信息
     * @return 格式化后的提示
     */
    public static String notExist(String entity) {
        return String.format(MESSAGE_NOT_EXIST_NO_PARAM, entity);
    }

    /**
     * xx: yy不存在<br/>
     * <p>
     * 例如：站点：秦林路站 不存在
     *
     * @param entity 实体信息, 必须配置{@link ApiModel}中的value属性
     * @param arg    实体参数
     * @return 格式化后的提示
     */
    public static String notExist(Class<?> entity, String arg) {
        return notExist(getModelValue(entity), arg);
    }

    /**
     * xx 不存在<br/>
     * <p>
     * 例如：站点不存在
     *
     * @param entity 实体信息, 必须配置{@link ApiModel}中的value属性
     * @return 格式化后的提示
     */
    public static String notExist(Class<?> entity) {
        return notExist(getModelValue(entity));
    }

    /**
     * 获取配置在class上{@link ApiModel}中的value属性
     *
     * @param clazz 指定的class
     * @return class上{@link ApiModel}中的value属性
     */
    private static String getModelValue(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        if (ENTITY_MODEL_NAME_MAP.containsKey(clazz.getName())) {
            return ENTITY_MODEL_NAME_MAP.get(clazz.getName());
        }

        ApiModel annotation = AnnotationUtil.findAnnotation(clazz, ApiModel.class);
        if (annotation == null) {
            return null;
        }

        ENTITY_MODEL_NAME_MAP.put(clazz.getName(), annotation.value());
        return annotation.value();
    }
}
