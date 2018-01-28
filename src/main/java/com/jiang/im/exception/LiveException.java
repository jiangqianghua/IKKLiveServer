package com.jiang.im.exception;

import com.jiang.im.enums.ResultEnum;

public class LiveException extends RuntimeException {
    private Integer code ;

    public LiveException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public LiveException(ResultEnum resultEnum,String message){
        super(message);
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


}