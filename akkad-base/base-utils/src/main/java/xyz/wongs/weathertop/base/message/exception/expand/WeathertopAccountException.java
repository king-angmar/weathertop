package xyz.wongs.weathertop.base.message.exception.expand;

import xyz.wongs.weathertop.base.message.enums.ResponseCode;
import xyz.wongs.weathertop.base.message.exception.GlobalException;

public class WeathertopAccountException extends GlobalException {

    public WeathertopAccountException(){
        super(ResponseCode.AUTHENTICATION_FAILED_ERROR.getMsg(), ResponseCode.AUTHENTICATION_FAILED_ERROR.getCode());
    }

    public WeathertopAccountException(String message, int code) {
        super(message, code);
    }

    public WeathertopAccountException(String message){
        super(message, ResponseCode.AUTHENTICATION_FAILED_ERROR.getCode());
    }
}
