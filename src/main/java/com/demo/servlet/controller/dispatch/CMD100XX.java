package com.demo.servlet.controller.dispatch;

import com.demo.servlet.controller.BaseRequestData;
import com.demo.servlet.exception.I18nException;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求处理模块1
 */
@Controller
public class CMD100XX {

    /**
     * 请求处理1
     */
    @CmdMapper(CodeConst.DEMO1)
    public Object DEMO1(BaseRequestData bean) {
        System.out.println("DISPATCH - " + bean.getCOMMON_PARAM().toString());
        return "This is 10000 Code DISPATCH Response";
    }

    /**
     * 请求处理2
     */
    @CmdMapper(CodeConst.DEMO2)
    public Object DEMO2(BaseRequestData bean) {
        System.out.println("DISPATCH - " + bean.getCOMMON_PARAM().toString());
        return "This is 10001 Code DISPATCH Response";
    }

    /**
     * 请求处理3
     */
    @CmdMapper(CodeConst.DEMO3)
    public Object DEMO3(BaseRequestData bean) {
        System.out.println("DISPATCH - " + bean.getCOMMON_PARAM().toString());
        return "This is 10002 Code DISPATCH Response";
    }

    /**
     * 请求处理4
     */
    @CmdMapper(CodeConst.DEMO4)
    public Object DEMO4(BaseRequestData bean) throws I18nException {
        throw new I18nException(0);
    }

    /**
     * 请求处理5
     */
    @CmdMapper(CodeConst.DEMO5)
    public Object DEMO5(BaseRequestData bean) throws I18nException {
        throw new I18nException(1, "DEMO5");
    }

    /**
     * 请求处理6
     */
    @CmdMapper(CodeConst.DEMO6)
    public Object DEMO6(BaseRequestData bean) throws I18nException {
        Map<String, Object> map = new HashMap<>();
        map.put("abc", "DEMO6");
        throw new I18nException(2, "DEMO6", map);
    }

    /**
     * 请求处理7
     */
    @CmdMapper(CodeConst.DEMO7)
    public Object DEMO7(BaseRequestData bean) throws I18nException {
        throw new I18nException(10000, "DEMO7");
    }

    /**
     * 请求处理8
     */
    @CmdMapper(CodeConst.DEMO8)
    public Object DEMO8(BaseRequestData bean) throws I18nException {
        throw new I18nException(20000, "DEMO8");
    }

    /**
     * 请求处理9
     */
    @CmdMapper(CodeConst.DEMO9)
    public Object DEMO9(BaseRequestData bean) throws I18nException {
        throw new I18nException(30000, "DEMO9");
    }

}
