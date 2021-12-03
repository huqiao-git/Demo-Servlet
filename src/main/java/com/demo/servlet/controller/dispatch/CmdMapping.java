package com.demo.servlet.controller.dispatch;

import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;

/**
 * CMD分发缓存结构体
 */
public class CmdMapping implements Mapping {

    private CmdMapper cmdMapper;

    private String methodName;

    private String beanName;

    private Class<?>[] paramterTypes;

    private BeanFactory beanFactory;

    public CmdMapping(CmdMapper cmdMapper, String methodName, String beanName, Class<?>[] paramterTypes, BeanFactory beanFactory) {
        this.cmdMapper = cmdMapper;
        this.methodName = methodName;
        this.beanName = beanName;
        this.paramterTypes = paramterTypes;
        this.beanFactory = beanFactory;
    }


    @Override
    public String getCommonCode() {
        return String.valueOf(this.cmdMapper.value());
    }

    @Override
    public Method getMethod() {
        try {
            return this.getBean().getClass().getMethod(this.methodName, paramterTypes);
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    @Override
    public Object getBean() {
        return this.beanFactory.getBean(this.beanName);
    }

}
