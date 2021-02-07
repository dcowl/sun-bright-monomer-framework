package org.sun.bright.framework.enums;

/**
 * 用户状态
 *
 * @author <a href="mailto:2867665887@qq.com">dcowl</a>
 * @version 1.0
 */
public enum UserStatus {
    /**
     * 禁用
     */
    OK(1, "正常"),
    /**
     * 禁用
     */
    DISABLE(0, "禁用"),
    /**
     * 删除
     */
    DELETED(-1, "删除");

    private final Integer code;
    private final String info;

    UserStatus(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer value() {
        return code;
    }

    public String message() {
        return info;
    }
}
