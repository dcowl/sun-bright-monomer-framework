package org.sun.bright.framework.core.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <b>Spring Ioc 依赖查找功能</b>
 * 实现 {@code ApplicationContextAware} 接口,
 * 实现 {@link #setApplicationContext(ApplicationContext)} 方法, 实现Bean的赋值操作;
 * {@code ApplicationContext} 对象必须由IOC管理,
 * 由 Spring 提供的注解 <code>@Component</code> 或其 派生注解声明的Bean
 * <p>
 * 方法中 {@link #setApplicationContext(ApplicationContext)} 会抛出异常: {@link org.springframework.beans.BeansException}
 * 调用方式1：可以通过Spring的依赖注入进行调用{@code SpringContextUtil}所提供的依赖查询方法
 * 调用方式2：可以直接通过{@code SpringContextUtil}调用依赖查询方法
 *
 * @author <a href="mailto:2867665887@qq.com">ShuNing</a>
 * @see SpringContextHolder
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取applicationContext
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     */
    public Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过 class (类型的方式)获取Bean.
     */
    public <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * 某 Bean 在IOC容器存在多个名称时使用该方法
     */
    public <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}