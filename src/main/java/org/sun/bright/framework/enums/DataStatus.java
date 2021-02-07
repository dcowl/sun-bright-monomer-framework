package org.sun.bright.framework.enums;

/**
 * 数据状态
 *
 * @author <a href="mailto:2867665887@qq.com">dcowl</a>
 * @version 1.0
 * @since 1.0
 */
public enum DataStatus {

    /**
     * 1 正常
     */
    OK(1, "正常"),
    /**
     * 0 停用
     */
    DISABLE(0, "停用"),
    /**
     * -1 删除
     */
    DELETED(-1, "删除");

    private final Integer code;

    private final String info;

    DataStatus(Integer code, String info) {
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
