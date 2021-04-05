package org.sun.bright.manager.validation;

import org.apache.commons.lang3.StringUtils;
import org.sun.bright.manager.validation.constant.ValidationConstant;
import org.sun.bright.manager.validation.annotation.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 手机号规则校验类
 *
 * @author <a href="mailto:2867665887@qq.com">SunBright</a>
 */
public class PhoneValidation implements ConstraintValidator<Phone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isNotEmpty(value)) {
            // matches()方法告知此字符串是否匹配给定的正则表达式。
            return Pattern.compile(ValidationConstant.PHONE_VALIDATION).matcher(value).matches();
        }
        return true;
    }

}
