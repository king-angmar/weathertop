package xyz.wongs.weathertop.base.message.exception.expand;

import xyz.wongs.weathertop.base.message.enums.ResponseCode;
import xyz.wongs.weathertop.base.message.exception.GlobalException;

public class ServerException extends GlobalException {

    public ServerException(String message){
        super(message, ResponseCode.ERROR.getCode());
    }
}
