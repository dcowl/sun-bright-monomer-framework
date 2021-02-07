package org.sun.bright.framework.result.enums;

import org.springframework.lang.Nullable;

/**
 * Result 相应数据类
 *
 * @author <a href="mailto:2867665887@qq.com">dcowl</a>
 * @version 1.0
 * @see org.springframework.http.HttpStatus
 */
public enum HttpStatus {

    // 2xx

    /**
     * 操作成功
     * {@code 200 操作成功}.
     */
    SUCCESS(200, "操作成功"),
    /**
     * 创建成功
     * {@code 201 创建成功}.
     */
    CREATE(201, "创建成功"),
    /**
     * 请求已经被接受
     * {@code 202 请求已经被接受}.
     */
    ACCEPT(202, "请求已经被接受"),
    /**
     * 操作已经执行成功，但是没有返回数据
     * {@code 204 操作已经执行成功，但是没有返回数据}.
     */
    NO_CONTENT(204, "操作已经执行成功，但是没有返回数据"),

    // 3xx

    /**
     * 资源已被移除
     * {@code 301 资源已被移除}.
     */
    MOVED_PERM(301, "资源已被移除"),
    /**
     * 重定向
     * {@code 303 重定向}.
     */
    REDIRECT(303, "重定向"),
    /**
     * 资源没有被修改
     * {@code 304 资源没有被修改}.
     */
    NOT_MODIFIED(304, "资源没有被修改"),

    // 4xx

    /**
     * 参数列表错误（缺少，格式不匹配）
     * {@code 400 请求参数列表错误！}.
     */
    BAD_REQUEST(400, "请求参数列表错误！"),
    /**
     * 未授权
     * {@code 401 未授权！}.
     */
    UNAUTHORIZED(401, "未授权"),
    /**
     * 访问受限，授权过期
     * {@code 403 访问受限，授权过期！}.
     */
    FORBIDDEN(403, "访问受限，授权过期"),
    /**
     * 资源，服务未找到
     * {@code 404 资源，服务未找到！}.
     */
    NOT_FOUND(404, "资源，服务未找到"),
    /**
     * 不允许的http方法
     * {@code 405 不允许的http方法！}.
     */
    BAD_METHOD(405, "不允许的http方法"),
    /**
     * 资源冲突，或者资源被锁
     * {@code 409 资源冲突，或者资源被锁！}.
     */
    CONFLICT(409, "资源冲突，或者资源被锁"),
    /**
     * 请求参数为空
     * {@code 410 请求参数为空！}.
     */
    PARAMETER_EMPTY(410, "请求参数为空！"),
    /**
     * 请求参数为空
     * {@code 413 请求参数错误！}.
     */
    PARAMETER_ERROR(413, "请求参数错误！"),
    /**
     * 不允许重复提交，请稍后再试
     * {@code 415 不允许重复提交，请稍后再试！}.
     */
    REPEAT_SUBMIT(415, "不允许重复提交，请稍后再试！"),
    /**
     * 不支持的数据，媒体类型
     * {@code 417 不支持的数据，媒体类型！}.
     */
    UNSUPPORTED_TYPE(417, "不支持的数据，媒体类型"),

    // 5xx

    /**
     * 系统内部错误
     * {@code 500 系统内部错误！}.
     */
    ERROR(500, "系统内部错误！"),
    /**
     * 接口未实现
     * {@code 501 接口未实现！}.
     */
    NOT_IMPLEMENTED(501, "接口未实现"),

    // unknown error

    /**
     * 未知错误
     * {@code -1 未知错误！}.
     */
    UNKNOWN_ERROR(-1, "未知错误！");

    public static final String CODE_TAG = "code";

    public static final String MSG_TAG = "msg";

    private final int value;

    private final String reasonPhrase;

    HttpStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * Return the integer value of this status code.
     */
    public int value() {
        return this.value;
    }

    /**
     * Return the reason phrase of this status code.
     */
    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public static String getDesc(int statusCode) {
        HttpStatus status = resolve(statusCode);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        }
        return status.getReasonPhrase();
    }

    @Override
    public String toString() {
        return this.value + " " + name();
    }

    public static HttpStatus valueOf(int statusCode) {
        HttpStatus status = resolve(statusCode);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        }
        return status;
    }

    @Nullable
    public static HttpStatus resolve(int statusCode) {
        for (HttpStatus status : values()) {
            if (status.value == statusCode) {
                return status;
            }
        }
        return null;
    }

}
