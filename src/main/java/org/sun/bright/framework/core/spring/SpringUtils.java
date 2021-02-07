package org.sun.bright.framework.core.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * spring工具类 方便在非spring管理环境中获取bean
 *
 * @author <a href="mailto:2867665887@qq.com">SunlightBright</a>
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Component
public final class SpringUtils implements BeanFactoryPostProcessor {

    private SpringUtils() {
    }

    /**
     * Spring应用上下文环境
     */
    private static ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory factory) {
        beanFactory = factory;
    }

    /**
     * 获取对象
     *
     * @param name bean name
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException BeansException
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        return (T) beanFactory.getBean(name);
    }

    /**
     * 获取类型为requiredType的对象
     *
     * @param clazz Class
     * @return 返回类型
     * @throws BeansException BeansException
     */
    public static <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name bean Name
     * @return boolean
     */
    public boolean containsBean(String name) {
        return beanFactory.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
     * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name Bean Name
     * @return boolean
     * @throws NoSuchBeanDefinitionException NoSuchBeanDefinitionException
     */
    public boolean isSingleton(String name) {
        return beanFactory.isSingleton(name);
    }

    /**
     * 根据 Bean name 获取 Bean Class 类型
     *
     * @param name Bean Name
     * @return Class 注册对象的类型
     * @throws NoSuchBeanDefinitionException NoSuchBeanDefinitionException
     */
    public Class<?> getType(String name) {
        return beanFactory.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name Bean Name
     * @return bean的别名
     * @throws NoSuchBeanDefinitionException NoSuchBeanDefinitionException
     */
    public String[] getAliases(String name) {
        return beanFactory.getAliases(name);
    }

    /**
     * 获取aop代理对象
     *
     * @return 代理对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getAopProxy() {
        return (T) AopContext.currentProxy();
    }
}
