package com.demo.servlet.controller.cmd;

import com.demo.servlet.controller.BaseRequestData;
import com.demo.servlet.exception.I18nException;
import com.demo.servlet.utils.CheckController;

/**
 * CMD类分发接口
 */
public abstract class BaseCmd {

    public Object baseExecute(BaseRequestData bean) throws I18nException {  //处理拦截
        CheckController.checkLogin(bean);
        CheckController.checkAuthority(bean);
        return execute(bean);
    }

    protected abstract Object execute(BaseRequestData bean);

}
