package com.eyinfo.webx.validator;

import com.eyinfo.foundation.utils.ConvertUtils;
import com.eyinfo.foundation.utils.GlobalUtils;
import com.eyinfo.foundation.utils.TextUtils;
import com.eyinfo.foundation.utils.ValidUtils;
import com.eyinfo.webx.annotations.LogicValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2020/9/25
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class LogicValidator implements ConstraintValidator<LogicValid, Object> {

    private String property;
    private String compareProperty;
    private String message;
    private String conditionRegexp;
    private String regexp;

    public void initialize(LogicValid logicValid) {
        this.property = logicValid.property();
        this.compareProperty = logicValid.compare();
        this.message = logicValid.message();
        this.conditionRegexp = logicValid.conditionRegexp();
        this.regexp = logicValid.regexp();
    }

    public boolean isValid(Object model, ConstraintValidatorContext constraintValidatorContext) {
        if (TextUtils.isEmpty(property) || TextUtils.isEmpty(compareProperty) || TextUtils.isEmpty(conditionRegexp) || TextUtils.isEmpty(regexp)) {
            return false;
        }
        if (TextUtils.isEmpty(message)) {
            message = "属性检验异常，请检查";
        }
        Object compareValue = GlobalUtils.getPropertiesValue(model, compareProperty);
        if (!ValidUtils.valid(conditionRegexp, ConvertUtils.toString(compareValue))) {
            //条件不成立即为验证通过
            return true;
        }
        Object currValue = GlobalUtils.getPropertiesValue(model, property);
        return ValidUtils.valid(regexp, ConvertUtils.toString(currValue));
    }
}
