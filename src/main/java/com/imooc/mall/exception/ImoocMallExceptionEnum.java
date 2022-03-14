package com.imooc.mall.exception;

public enum ImoocMallExceptionEnum {
    NEED_USER_NAME(10001, "user name could not be empty"),
    NEED_PASSWORD(10002,"password could not be empty"),
    PASSWORD_TOO_SHORT(10003, "Password is too short"),
    NAME_EXISTED(10004,"name already exists"),
    INSERT_FAILED(10005, "insert failed"),
    WRONG_PASSWORD(10006,"wrong password"),
    NEED_LOGIN(10007, "need login"),
    UPDATE_FAILED(10008,"update failed"),
    NEED_ADMIN(10009,"not admin"),
    PARA_NOT_NULL(10010,"parameters should not be null"),
    CREATE_FAILED(10011,"creation failed"),
    REQUEST_PARAM_ERROR(10012,"request parameters error"),
    DELETE_FAILED(10013,"delete failed"),
    SYSTEM_ERROR(20000,"system error");



    Integer code;
    String msg;

    ImoocMallExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
