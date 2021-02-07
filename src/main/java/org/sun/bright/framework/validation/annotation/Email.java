package org.sun.bright.framework.validation.annotation;

import org.sun.bright.framework.validation.EmailValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 手机号验证
 *
 * <a href="mailto:2867665887@qq.com">SunBright</a>
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EmailValidation.class})
public @interface Email {

    String[] value() default {};

    String message() default "邮箱地址格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
