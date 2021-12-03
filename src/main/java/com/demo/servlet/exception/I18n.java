package com.demo.servlet.exception;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;

public class I18n {

    private static final Map<String, ResourceBundle> rbMap = new HashMap<>();

    private static ResourceBundle getResourceBundle(String key) {  //拆分多模块配置文件
        String resourceName = "i18n/";
        if (StringUtils.isNumeric(key)) {
            if (Integer.parseInt(key) < 10000) {
                resourceName += "common";
            } else {
                resourceName += key.charAt(0) + "0000";
            }
        } else {
            resourceName += "common";
        }
        synchronized (rbMap) {
            ResourceBundle rb = rbMap.get(resourceName);
            if (rb == null) {
                rb = ResourceBundle.getBundle(resourceName, Locale.getDefault());
                rbMap.put(resourceName, rb);
            }
            return rb;
        }

    }

    public static String getMsg(String key) {
        if (key == null) {
            return "";
        }
        try {
            ResourceBundle rb = getResourceBundle(key);
            if (rb == null) {
                return "";
            }
            String sMsg = String.valueOf(rb.getObject(key));
            if (StringUtils.isBlank(sMsg)) {
                return "";
            }
            return sMsg;
        } catch (Exception ex) {
            return "";
        }
    }

    public static String i18Translate(I18nException exception, String msg) {
        Integer excCode = exception.getExcCode();
        String message;
        Map<String, Object> map = new HashMap<>(exception.getExcInfo());
        map.put("_Code", excCode);
        map.put("_Message", msg);
        message = I18n.i18Translate(excCode, msg, map);
        return message;
    }

    public static String i18Translate(Integer code, String defaultMessage, Map<String, Object> paraMap) {
        if (defaultMessage == null) {
            defaultMessage = "";
        }
        if (code == null) {
            return defaultMessage;
        }
        String i18Code = I18n.getMsg(String.valueOf(code));
        String msg = StrSubstitutor.replace(i18Code, paraMap);
        if (StringUtils.isBlank(msg)) {
            msg = defaultMessage;
        }
        return msg;
    }

}
