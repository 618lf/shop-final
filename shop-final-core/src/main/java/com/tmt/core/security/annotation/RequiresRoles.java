package com.tmt.core.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 新版权限标示
 *
 * @author: lifeng
 * @date: 2020/3/28 17:22
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresRoles {

    /**
     * 需要的角色
     */
    String[] value() default {};

    /**
     * The logical operation for the permission check in case multiple roles are specified. AND is the default
     */
    Logical logical() default Logical.AND;
}
