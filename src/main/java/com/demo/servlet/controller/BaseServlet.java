package com.demo.servlet.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo.servlet.controller.cmd.BaseCmd;
import com.demo.servlet.controller.dispatch.BaseDispatcher;
import com.demo.servlet.controller.dispatch.Mapping;
import com.demo.servlet.exception.I18nException;
import com.demo.servlet.utils.SpringContextHelper;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

public class BaseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private BaseDispatcher baseDispatcher;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        ctx.getServletContext(); //TODO 初始化管理Context,Session
        baseDispatcher = new BaseDispatcher();
        baseDispatcher.init();
        logger.info("服务端初始化...");
    }

    @Override
    public void destroy() {
        logger.info("服务端即将关闭...");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        process(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        process(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        String data = request.getParameter("data");
        BaseRequestMo requestMo = JSON.parseObject(data, BaseRequestMo.class);
        logger.info("请求接口协议编号 ---> : [ {} ]", requestMo.getCode());
        logger.info("请求参数 --->: \n{}", data);

        BaseResponseVo responseVo = new BaseResponseVo();

        /* 分发处理 */
        Object resultObj = null;
        try {
            //checkLogin(request, requestMo.getCode());  //登录检查
            CmdMapper mapper = baseDispatcher.getMapper(requestMo);
            if (mapper == null) {  //类名分发 (不支持 / - 符号)
                BaseCmd cmd = (BaseCmd) SpringHelper.getBean("CMD" + requestMo.getCode());
                resultObj = cmd.baseExecute(requestMo);
            } else {  //注解分发
                Method m = mapper.getMethod();
                Class<?>[] clazzs = m.getParameterTypes();
                Object[] values = new Object[clazzs.length];
                for (int i = 0; i < clazzs.length; i++) {
                    Class<?> clazz = clazzs[i];
                    if (clazz == HttpServletRequest.class) {
                        values[i] = request;
                    } else if (clazz == HttpServletResponse.class) {
                        values[i] = response;
                    } else if (clazz == BaseRequestMo.class) {
                        values[i] = requestMo;
                    } else {
                        values[i] = JsonUtil.toObjBean(requestMo.getData(), clazz);
                    }
                }
                resultObj = m.invoke(mapper.getBean(), values);
            }
        } catch (NoSuchBeanDefinitionException e) {
            responseVo.setCode(-1);
            responseVo.setMessage("无此接口编号");
        } catch (I18nException e) {
            responseVo.setCode(e.getExcCode());
            responseVo.setMessage(e.getMessage());
        } catch (Exception e) {
            responseVo.setCode(-9);
            responseVo.setMessage("接口导致系统错误");
        }
        if (resultObj instanceof HSSFWorkbook) {
            HSSFWorkbook wb = (HSSFWorkbook) resultObj;
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
                response.setHeader("contentType", "application/vnd.ms-excel");
                String filename = DateFormatUtils.format(Calendar.getInstance().getTime(), "yyyy_MM_dd_HH_mm_ss");
                filename = "report_" + filename;
                response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".xls");
                wb.write(baos);
                response.setContentLength(baos.size());
                OutputStream out = response.getOutputStream();
                baos.writeTo(out);
                logger.info("响应Excel ---> : {}", filename);
                out.close();
            } catch (IOException e) {
                logger.error("无法输出Excel, cause: " + e.getMessage(), e);
            }
        } else {
            responseVo.setData(resultObj);
            String result = JSON.toJSONString(responseVo, SerializerFeature.WriteMapNullValue);
            logger.info("响应数据 ---> : \n{}", result);
            try {
                PrintWriter out = response.getWriter();
                out.print(result);
                out.flush();
                out.close();
            } catch (IOException e) {
                logger.error("无法响应, cause: " + e.getMessage(), e);
            }
        }
    }
}
