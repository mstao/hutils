package me.mingshan.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Json 工具类，封装JsonSon
 *
 * @author hanjuntao
 */
public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // 对象的所有字段全部列入
        MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // 取消默认转换timestamps形式
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 忽略空Bean转json的错误
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        MAPPER.setDateFormat(new SimpleDateFormat(TimeUtil.TimeFormat.LONG_DATE_PATTERN_LINE_24.getPattern()));
        // 忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 对象转为json字符串
     *
     * @param source 源对象
     * @return 转后的json字符串
     */
    public static String objectToString(Object source) {
        if (source == null) {
            return null;
        }

        try {
            return MAPPER.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转Javabean
     *
     * @param json        待转字符串
     * @param targetClass 目标bean
     * @return 转换后的对象
     */
    public static <T> T jsonToObject(String json, Class<T> targetClass) {
        try {
            return MAPPER.readValue(json, targetClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转Map
     *
     * @param json   待转字符串
     * @param kClass Key 的class
     * @param vClass Value 的class
     * @param <K>    Kay 泛型
     * @param <V>    Value 泛型
     * @return 转换后的Map
     */
    public static <K, V> Map<K, V> jsonToMap(String json, Class<K> kClass, Class<V> vClass)
            throws JsonProcessingException {
        try {
            JavaType javaType = MAPPER.getTypeFactory().constructParametricType(kClass, vClass);
            return MAPPER.readValue(json, javaType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
