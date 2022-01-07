package me.mingshan.util.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Spring bean工具类
 *
 * @author hanjuntao
 * @date 2021/9/11
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    /**
     * 上下文对象
     */
    private static ApplicationContext apt;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        apt = applicationContext;
    }

    /**
     * 通过clazz,从spring容器中获取bean
     *
     * @param clazz 指定类
     * @param <T>   泛型信息
     * @return 容器中的对象
     */
    public static <T> T getBean(Class<T> clazz) {
        return apt.getBean(clazz);
    }

    /**
     * 获取某一类型的bean集合
     *
     * @param clazz 指定类
     * @param <T>   泛型信息
     * @return 容器中的对象集合
     */
    public static <T> Map<String, T> getBeans(Class<T> clazz) {
        return apt.getBeansOfType(clazz);
    }

    /**
     * 通过name和clazz,从spring容器中获取bean
     *
     * @param name  指定名称
     * @param clazz 指定类
     * @param <T>   泛型
     * @return 容器中的对象
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return apt.getBean(name, clazz);
    }

    /**
     * 获取配置文件配置项的值
     *
     * @param key 配置项key
     */
    public static String getEnvironmentProperty(String key) {
        return apt.getEnvironment().getProperty(key);
    }

    /**
     * 获取spring.profiles.active
     */
    public static String[] getActiveProfile() {
        return apt.getEnvironment().getActiveProfiles();
    }

    /**
     * 获取Spring容器上下文
     *
     * @return Spring容器
     */
    public static ApplicationContext getContext() {
        return apt;
    }
}
