package com.sang.demoaop.architect.annotation;


import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemControllerLog {

    String description() default "";

}
