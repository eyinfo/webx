package com.eyinfo.webx.annotations;

import java.lang.annotation.*;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/14
 * Description:
 * Modifier:
 * ModifyContent:
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GSModuleUnique {
    /**
     * 模块标识
     *
     * @return
     */
    String unique();

    /**
     * 描述
     *
     * @return
     */
    String describe();
}
