package com.miaosha.error;

public interface CommonError {
    public Integer getErrorCode();
    public String getErrorMsg();
    public CommonError setErrorMsg(String errorMsg);
}
