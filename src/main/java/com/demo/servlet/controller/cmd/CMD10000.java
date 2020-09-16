package com.demo.servlet.controller.cmd;

import com.demo.servlet.controller.BaseRequestData;
import org.springframework.stereotype.Component;

/**
 * 请求处理1
 */
@Component
public class CMD10000 extends BaseCmd {

    @Override
    protected Object execute(BaseRequestData bean) {
        System.out.println("CMD - " + bean.getCOMMON_PARAM().toString());
        return "This is 10000 Code CMD Response";
    }

}
