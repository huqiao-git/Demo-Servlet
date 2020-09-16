package com.demo.servlet.controller.dispatch;

import java.lang.reflect.Method;

/**
 * CMD分发缓存结构体接口
 */
public interface Mapping {

    String getCommonCode();

    Method getMethod();

    Object getBean();

}
