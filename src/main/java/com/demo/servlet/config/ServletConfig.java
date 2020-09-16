package com.demo.servlet.config;

import com.demo.servlet.controller.BaseServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {

    /**
     * 统一接口配置
     */
    @Bean(name = "BaseServlet")
    public ServletRegistrationBean<BaseServlet> getBaseServlet() {
        BaseServlet bs = new BaseServlet();
        return new ServletRegistrationBean<>(bs, "/huqiao");
    }

}
