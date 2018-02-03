package com.jiang.im.enums;

public enum ResultEnum {
    SUCCESS(0,"成功"),
    USER_LOGIN_ERROR(-100,"操作失败，系统内部错误"),
    USER_NOT_FOUND(-101,"用户不存在"),
    USER_NOT_PASSWORD(-102,"密码错误"),
    USER_EXPIRE(-103,"用户登陆过期"),
    USER_EXISTS(-104,"用户已经存在"),
    USER_REGISTER_ERROR(-105,"用户注册失败"),
    USER_UPDATE_ERR(-106,"用户更新失败"),
    ROOM_NOT_FOUND(-201,"房间不存在"),
    ;
    private Integer code ;
    private String message ;

    ResultEnum(Integer code , String message) {
        this.code = code ;
        this.message = message ;

    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
