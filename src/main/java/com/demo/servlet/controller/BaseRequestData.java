package com.demo.servlet.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * HTTP请求结构体
 */
public class BaseRequestData {

    @JSONField(name = "COMMON_CODE")
    private String COMMON_CODE;
    @JSONField(name = "COMMON_PARAM")
    private JSON COMMON_PARAM;
    @JSONField(name = "COMMON_TICKET")
    private String COMMON_TICKET;

    public String getCOMMON_CODE() { return COMMON_CODE; }
    public void setCOMMON_CODE(String COMMON_CODE) { this.COMMON_CODE = COMMON_CODE; }
    public JSON getCOMMON_PARAM() { return COMMON_PARAM; }
    public void setCOMMON_PARAM(JSON COMMON_PARAM) { this.COMMON_PARAM = COMMON_PARAM; }
    public String getCOMMON_TICKET() { return COMMON_TICKET; }
    public void setCOMMON_TICKET(String COMMON_TICKET) { this.COMMON_TICKET = COMMON_TICKET; }

}
