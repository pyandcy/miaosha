package com.miaosha.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidationImpl implements InitializingBean {
    private Validator validator;

//    实现校验方法并返回校验结果
    public ValidationResult validate(Object bean){
        final ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
        if (constraintViolationSet.size()>0){
            result.setHasError(true);
            constraintViolationSet.forEach(constraintViolation->{
                String errorMsg= constraintViolation.getMessage();
                String propertyName = constraintViolation.getPropertyPath().toString();
                result.getErrorMsgMap().put(propertyName,errorMsg);
            });
        }
        return result;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
//        将hebirnate validate通过工厂的实例化方式使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

    }
}
