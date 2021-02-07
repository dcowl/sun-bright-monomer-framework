package org.sun.bright.exception.codec;

import org.springframework.lang.NonNull;
import org.sun.bright.framework.message.MessageUtils;

import java.util.Objects;

/**
 * Codec Exception
 *
 * @author <a href="mailto:2867665887@qq.com">SunlightBright</a>
 * @version 1.0
 * @since 2021/2/1
 */
public class CodecException extends RuntimeException {

    private static final long serialVersionUID = 136710260731863817L;

    public static final String CODE_TAG = "codec.error";

    private final String message;

    public CodecException(@NonNull String defaultMessage) {
        String msg = MessageUtils.message(CODE_TAG);
        if (Objects.nonNull(msg)) {
            this.message = msg;
        } else {
            this.message = defaultMessage;
        }
    }

    public CodecException(String code, String defaultMessage) {
        String msg = MessageUtils.message(code);
        if (Objects.nonNull(msg)) {
            this.message = msg;
        } else {
            this.message = defaultMessage;
        }
    }

    public CodecException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
