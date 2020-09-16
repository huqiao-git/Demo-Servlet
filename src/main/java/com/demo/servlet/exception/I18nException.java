package com.demo.servlet.exception;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class I18nException extends Exception {

    private static final long serialVersionUID = -6860608104710227587L;

    private final int excCode;

    private final Map<String, Object> excInfo = new LinkedHashMap<>();

    public I18nException(int excCode, String excMsg, Map<String, Object> excInfo) {
        super(excMsg);
        this.excCode = excCode;
        this.excInfo.putAll(excInfo);
    }

    public I18nException(int excCode, Throwable cause) {
        super(cause);
        this.excCode = excCode;
    }

    public I18nException(int excCode, String excMsg) {
        super(excMsg);
        this.excCode = excCode;
    }

    public I18nException(int excCode) {
        this.excCode = excCode;
    }

    public int getExcCode() {
        return excCode;
    }

    public Map<String, Object> getExcInfo() {
        return Collections.unmodifiableMap(excInfo);
    }

    public String getMessage() {
        try {
            return I18n.i18Translate(this, super.getMessage());
        } catch (Exception ex) {
            return super.getMessage();
        }
    }
}
