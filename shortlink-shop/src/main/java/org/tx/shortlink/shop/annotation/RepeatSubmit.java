package org.tx.shortlink.shop.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {
    enum Type { PARAM, TOKEN }
    Type limitType() default Type.TOKEN;

    long lockTime() default 5;
}
