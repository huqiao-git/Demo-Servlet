package com.demo.servlet.controller.dispatch;

import java.lang.annotation.*;

/**
 * CMD分发注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CmdMapper {

    int value();

}

