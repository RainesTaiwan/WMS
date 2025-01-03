package com.sap.ewm.core.security.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Inherited
@Documented
public @interface Secured {

    String value() default "";

    String authType() default "MENU";

    boolean loginCheck() default true;
}