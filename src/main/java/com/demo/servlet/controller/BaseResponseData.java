package com.demo.servlet.controller;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * HTTP响应结构体
 */
public class BaseResponseData {

    @JSONField(name = "COMMON_ERR_ID")
    private int COMMON_ERR_ID;
    @JSONField(name = "COMMON_ERR_MSG")
    private String COMMON_ERR_MSG;
    @JSONField(name = "COMMON_DATA")
    private Object COMMON_DATA;
    @JSONField(name = "COMMON_DATA2")
    private Object COMMON_DATA2;

    public int getCOMMON_ERR_ID() { return COMMON_ERR_ID; }
    public void setCOMMON_ERR_ID(int COMMON_ERR_ID) { this.COMMON_ERR_ID = COMMON_ERR_ID; }
    public String getCOMMON_ERR_MSG() { return COMMON_ERR_MSG; }
    public void setCOMMON_ERR_MSG(String COMMON_ERR_MSG) { this.COMMON_ERR_MSG = COMMON_ERR_MSG; }
    public Object getCOMMON_DATA() { return COMMON_DATA; }
    public void setCOMMON_DATA(Object COMMON_DATA) { this.COMMON_DATA = COMMON_DATA; }
    public Object getCOMMON_DATA2() { return COMMON_DATA2; }
    public void setCOMMON_DATA2(Object COMMON_DATA2) { this.COMMON_DATA2 = COMMON_DATA2; }
}
