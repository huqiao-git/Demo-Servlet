package com.demo.servlet.controller.dispatch;

import com.demo.servlet.controller.BaseRequestData;
import com.demo.servlet.exception.I18nException;
import com.demo.servlet.utils.CheckController;
import com.demo.servlet.utils.SpringContextHelper;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * CMD注解分发
 */
public class BaseDispatcher {

    private boolean isInit = false;

    private Map<String, Mapping> mappings = new HashMap<>();  //CMD注解的函数集

    public void init() {
        if (isInit) {
            return;
        }
        initMapper();
        isInit = true;
    }

    private void initMapper() {
//        String[] beanNames = SpringContextHelper.getApplicationContext().getBeanDefinitionNames();  //扫描全部Spring管理的类
        String[] beanNames = SpringContextHelper.getBeanNamesForAnnotation(Controller.class);  //扫描Spring某一注解的类
        for (String beanName : beanNames) {
            Object bean = SpringContextHelper.getBean(beanName);
            Class<?> clazz = bean.getClass();
            Method[] ms = clazz.getMethods();
            for (Method m : ms) {
                if (isActionMethod(m)) { //过滤掉不是CMD注解的函数
                    CmdMapper cmdMapper = AnnotationUtils.findAnnotation(m, CmdMapper.class);
                    Mapping ac = new CmdMapping(cmdMapper, m.getName(), beanName, m.getParameterTypes(), SpringContextHelper.getApplicationContext());
                    mappings.put(ac.getCommonCode(), ac);
                }
            }
        }
    }

    public Mapping getMapping(BaseRequestData bean) throws I18nException {  //处理拦截
        CheckController.checkLogin(bean);
        CheckController.checkAuthority(bean);
        return mappings.get(bean.getCOMMON_CODE());
    }

    private boolean isActionMethod(Method m) {
        CmdMapper cmdMapper = AnnotationUtils.findAnnotation(m, CmdMapper.class);
        if (cmdMapper == null || Modifier.isStatic(m.getModifiers())) {
            return false;
        }
        Class<?>[] argTypes = m.getParameterTypes();
        if (argTypes.length == 0) {
            return true;
        }
        return true;
    }

}
