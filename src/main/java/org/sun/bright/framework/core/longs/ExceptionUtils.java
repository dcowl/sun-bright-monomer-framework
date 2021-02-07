package org.sun.bright.framework.core.longs;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类.
 *
 * @author <a href="mailto:2867665887@qq.com">dcowl</a>
 * @version 1.0
 */
public class ExceptionUtils {

    private ExceptionUtils() {
    }

    private static final String EXCEPTION = "exception";

    private static final String SERVLET_ERROR = "javax.servlet.error.exception";

    /**
     * 在request中获取异常类
     *
     * @param request HttpServletRequest
     * @return Throwable
     */
    public static Throwable getThrowable(HttpServletRequest request) {
        if (request.getAttribute(EXCEPTION) != null) {
            return (Throwable) request.getAttribute(EXCEPTION);
        } else if (request.getAttribute(SERVLET_ERROR) != null) {
            return (Throwable) request.getAttribute(SERVLET_ERROR);
        }
        return null;
    }

    /**
     * 将 ErrorStack 转化为 {@link String}.
     *
     * @param e Throwable
     */
    public static String getStackTraceAsString(Throwable e) {
        if (e == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 判断异常是否由某些底层的异常引起.
     * <pre>
     *     {@code @SafeVarargs} 在JDK7中引入，主要目的是处理可变长参数中的泛型，
     *     此注解告诉编译器：在可变长参数中的泛型是类型安全的。可变长参数是使用数组存储的，而数组和泛型不能很好的混合使用。
     * </pre>
     */
    @SafeVarargs
    public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
        Throwable cause = ex.getCause();
        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeExceptionClasses) {
                if (causeClass.isInstance(cause)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }

    /**
     * 将 {@code CheckedException} 转换为 {@code UncheckedException} .
     */
    public static RuntimeException unchecked(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

}
