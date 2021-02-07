package org.sun.bright.framework.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义 MVC 拦截器
 *
 * @author <a href="mailto:2867665887@qq.com">SunBright</a>
 */
@Slf4j
@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response,
                             @Nullable Object handler) throws Exception {
        // 在 controller 方法之前调用
        return true;
    }

    @Override
    public void postHandle(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response,
                           @Nullable Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        // 在 controller 方法之后调用
    }

    @Override
    public void afterCompletion(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response,
                                @Nullable Object handler, @Nullable Exception ex) throws Exception {
        // 在 postHandle 方法之后调用
    }

}
