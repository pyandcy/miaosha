package com.miaosha.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ValidationResult {
    private boolean hasError=false;
    private Map<String,String> errorMsgMap=new HashMap<>();

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }
//    实现通用的通过格式化字符串信息获取错误结果的msg信息
    public String getErrorMsg(){
        return StringUtils.join(errorMsgMap.values().toArray(),",");
    }
}
