package com.demo.servlet.controller.cmd;

import com.demo.servlet.controller.BaseRequestData;
import org.springframework.stereotype.Component;

/**
 * 请求处理2
 */
@Component
public class CMD10001 extends BaseCmd {
    @Override
    protected Object execute(BaseRequestData bean) {
        System.out.println("CMD - " + bean.getCOMMON_PARAM().toString());
        return "This is 10001 Code CMD Response";
    }
}
