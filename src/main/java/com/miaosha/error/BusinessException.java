package com.miaosha.error;
//包装类业务异常实现
public class BusinessException extends Exception implements CommonError {
    private CommonError commonError;
//直接接受EmBusinessError的传参用于构建业务异常
    public BusinessException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }
//接受自定义的errorMsg用于构造异常

    public BusinessException( CommonError commonError,String errorMsg) {
        super();
        this.commonError = commonError;
        this.commonError.setErrorMsg(errorMsg);
    }

    @Override
    public Integer getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrorMsg() {
        return this.commonError.getErrorMsg();
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.commonError.setErrorMsg(errorMsg);
        return this;
    }
}
