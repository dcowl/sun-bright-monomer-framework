package org.sun.bright.manager.validation.constant;

/**
 * 参数校验常量类
 *
 * @author <a href="mailto:2867665887@qq.com">SunBright</a>
 */
public class ValidationConstant {

    private ValidationConstant() {
    }

    /**
     * 手机号码正则校验表达式
     */
    public static final String PHONE_VALIDATION = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\\\d{8}$";

    /**
     * 邮箱地址正则校验表达式
     */
    public static final String EMAIL_VALIDATION = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    /**
     * 密码正则校验表达式
     */
    public static final String PASSWORD_VALIDATION = "^^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*_-]+$)(?![a-zA-z\\d]+$)(?![a-zA-z!@#$%^&*_-]+$)(?![\\d!@#$%^&*_-]+$)[a-zA-Z\\d!@#$%^&*_-]+$";

    /**
     * 只包含数据或字母
     */
    public static final String PASSWORD_NUM_OR_CHARSET = "^(?:\\d+|[a-zA-Z]+|[!@#$%^&*]+)$";

    /**
     * 同时包含 数字 、 字母
     */
    public static final String PASSWORD_NUM_AND_CHARSET = "^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+$";

}
