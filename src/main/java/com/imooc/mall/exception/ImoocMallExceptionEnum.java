package com.imooc.mall.exception;

public enum ImoocMallExceptionEnum {
  NEED_USER_NAME(10001, "user name could not be empty"),
  NEED_PASSWORD(10002, "password could not be empty"),
  PASSWORD_TOO_SHORT(10003, "Password is too short"),
  NAME_EXISTED(10004, "name already exists"),
  INSERT_FAILED(10005, "insert failed"),
  WRONG_PASSWORD(10006, "wrong password"),
  NEED_LOGIN(10007, "need login"),
  UPDATE_FAILED(10008, "update failed"),
  NEED_ADMIN(10009, "not admin"),
  PARA_NOT_NULL(10010, "parameters should not be null"),
  CREATE_FAILED(10011, "creation failed"),
  REQUEST_PARAM_ERROR(10012, "request parameters error"),
  DELETE_FAILED(10013, "delete failed"),
  MKDIR_FAILED(10014, "mkdir failed"),
  UPLOAD_FAILED(10015, "upload image failed"),
  NOT_SALE(10016, "product can not be sold"),
  NOT_ENOUGH(10017, "lack enough products"),
  CART_EMPTY(10018, "empty cart"),
  NO_ENUM(10019, "no such enum"),
  NO_ORDER(10020, "no such order"),
  NOT_YOUR_ORDER(10021, "not your order"),
  WRONG_ORDER_STATUS(10022, "wrong order status"),
  SYSTEM_ERROR(20000, "system error"),
  ;

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
