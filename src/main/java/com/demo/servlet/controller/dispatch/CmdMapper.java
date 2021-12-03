package com.demo.servlet.controller.dispatch;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

/**
 * CMD分发注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = {RequestMethod.POST})
public @interface CmdMapper {

    @AliasFor(annotation = RequestMapping.class)
    String value();

}

