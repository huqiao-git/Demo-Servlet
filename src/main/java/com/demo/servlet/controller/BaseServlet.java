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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) { process(request, response); }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) { process(request, response); }

    private void process(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        String data = request.getParameter("data");
        BaseRequestData requestData = JSON.parseObject(data, BaseRequestData.class);
        logger.info("请求接口协议编号 ---> : [ " + requestData.getCOMMON_CODE() + " ]");
        logger.info("请求参数 --->: \n" + data);

        BaseResponseData responseDara = new BaseResponseData();
        //TODO 类分发
        Object resultObj = null;
        try {
            BaseCmd cmd = (BaseCmd) SpringContextHelper.getBean("CMD" + requestData.getCOMMON_CODE());
            resultObj = cmd.baseExecute(requestData);
        } catch (I18nException e) {
            responseDara.setCOMMON_ERR_ID(e.getExcCode());
            responseDara.setCOMMON_ERR_MSG(e.getCause().getMessage());
        } catch (Exception e) {
            responseDara.setCOMMON_ERR_ID(1);
            responseDara.setCOMMON_ERR_MSG("无此接口编号");
        }
        //TODO 注解分发
        Object resultObj2 = null;
        try {
            Mapping mapping = baseDispatcher.getMapping(requestData);
            Method m = mapping.getMethod();
            resultObj2 = m.invoke(mapping.getBean(), requestData);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof I18nException) {
                responseDara.setCOMMON_ERR_ID(((I18nException) e.getTargetException()).getExcCode());
                responseDara.setCOMMON_ERR_MSG(e.getCause().getMessage());
            } else {
                responseDara.setCOMMON_ERR_ID(2);
                responseDara.setCOMMON_ERR_MSG("无此接口编号2");
            }
        } catch (I18nException e) {
            responseDara.setCOMMON_ERR_ID(e.getExcCode());
            responseDara.setCOMMON_ERR_MSG(e.getCause().getMessage());
        } catch (Exception e) {
            responseDara.setCOMMON_ERR_ID(2);
            responseDara.setCOMMON_ERR_MSG("无此接口编号2");
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
                logger.info("响应Excel ---> : " + filename);
                out.close();
            } catch (IOException e) {
                logger.error("无法输出Excel, cause: " + e.getMessage(), e);
            }
        } else {
            responseDara.setCOMMON_DATA(resultObj);
            responseDara.setCOMMON_DATA2(resultObj2);
            String result = JSON.toJSONString(responseDara, SerializerFeature.WriteMapNullValue);
            logger.info("响应数据 ---> : \n" + result);
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
