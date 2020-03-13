package com.miaosha.response;

public class CommonReturnType {
//    通过状态status判定该返回何值
//    statu=success则data返回前端需要的json数据
//    status=fail则data返回状态码
    private String status;
    private Object data;
//定义一个通用的创建方法
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }
    public static CommonReturnType create(Object result,String status){
        CommonReturnType type = new CommonReturnType();
        type.setData(result);
        type.setStatus(status);
        return type;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
