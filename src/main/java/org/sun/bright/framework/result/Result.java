package org.sun.bright.framework.result;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.sun.bright.framework.result.enums.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * http 响应 result
 *
 * <pre>
 *  1、transient关键字只能修饰变量，而不能修饰方法和类。注意，本地变量是不能被transient关键字修饰的。
 *  2、被transient关键字修饰的变量不再能被序列化，一个静态变量不管是否被transient修饰，均不能被序列化。
 *  3、一旦变量被transient修饰，变量将不再是对象持久化的一部分，该变量内容在序列化后无法获得访问。
 *     也可以认为在将持久化的对象反序列化后，被transient修饰的变量将按照普通类成员变量一样被初始化。
 * </pre>
 *
 * @author <a href="mailto:2867665887@qq.com">SunBright</a>
 */
@Slf4j
public final class Result implements Serializable {

    private static final long serialVersionUID = -1089358352297441427L;

    /**
     * 状态值
     */
    @Getter
    private final Integer code;

    /**
     * 消息
     */
    @Getter
    private final String msg;

    /**
     * 数据
     */
    @Getter
    private Object data;

    private Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result ok() {
        return new Result(HttpStatus.SUCCESS.value(), HttpStatus.SUCCESS.getReasonPhrase());
    }

    public static Result ok(HttpStatus status, Object data) {
        return new Result(status.value(), status.getReasonPhrase(), data);
    }

    public static Result build(HttpStatus status) {
        return new Result(status.value(), status.getReasonPhrase());
    }

    public static Result build(HttpStatus status, String msg) {
        return new Result(status.value(), msg);
    }

    public static Result error() {
        return new Result(HttpStatus.ERROR.value(), HttpStatus.ERROR.getReasonPhrase());
    }

    /**
     * 放入单个自定义业务数据并返回自己
     */
    public Result putRecord(String key, Object value) {
        Map<String, Object> map = new HashMap<>(10);
        map.put(key, value);
        this.data = map;
        return this;
    }
}
