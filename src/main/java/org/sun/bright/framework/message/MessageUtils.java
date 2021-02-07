package org.sun.bright.framework.message;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.sun.bright.framework.core.spring.SpringUtils;

/**
 * 获取i18n资源文件
 *
 * @author <a href="mailto:2867665887@qq.com">dcowl</a>
 * @version 1.0
 */
public class MessageUtils {

    private MessageUtils() {
    }

    private static final MessageSource MESSAGE = SpringUtils.getBean(MessageSource.class);

    /**
     * 根据消息键和参数获取消息，委托给spring messageSource
     * <code>
     * // 使用 {@link org.sun.bright.framework.core.spring.SpringContextHolder} 进行运算
     * MessageSource bean = SpringContextHolder.getApplicationContext().getBean(MessageSource.class);
     * </code>
     *
     * @param code 消息键
     * @param args 数组参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, Object... args) {
        if (null == code || "".equals(code)) {
            return null;
        }
        return MESSAGE.getMessage(code, args, LocaleContextHolder.getLocale());
    }

}
