package com.eyinfo.webx.annotations;

import com.eyinfo.webx.validator.LogicValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2020/9/25
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = LogicValidator.class)
@Documented
public @interface LogicValid {

    //提示消息
    String message();

    //当前属性
    String property();

    //比较属性
    String compare();

    //条件正则
    String conditionRegexp();

    //正则
    String regexp();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        LogicValid[] value();
    }
}
