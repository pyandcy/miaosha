package com.miaosha.controller;

import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public class BaseController {
    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";
    //    定义exceptionhandle解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex){
        HashMap<String, Object> resultData = new HashMap<>();
        if (ex instanceof BusinessException){
            BusinessException businessException=(BusinessException)ex;
            resultData.put("errorCode",businessException.getErrorCode());
            resultData.put("errorMsg",businessException.getErrorMsg());
        }else {
            resultData.put("errorCode",EmBusinessError.UNKNOWN_ERROR.getErrorCode());
            resultData.put("errorMsg",EmBusinessError.UNKNOWN_ERROR.getErrorMsg());
        }
        return CommonReturnType.create(resultData,"fail");
    }
}
