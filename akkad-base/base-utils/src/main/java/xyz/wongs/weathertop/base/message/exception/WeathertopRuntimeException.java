package xyz.wongs.weathertop.base.message.exception;

import xyz.wongs.weathertop.base.message.enums.ResponseCode;


/**
 * @ClassName WeathertopRuntimeException
 * @Description 自定义异常
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2019/9/21 10:07
 * @Version 1.0.0
*/
public class WeathertopRuntimeException extends RuntimeException{

    private static final long serialVersionUID = -6370612186038915645L;

    private final ResponseCode response;

    public WeathertopRuntimeException(ResponseCode response) {
        this.response = response;
    }
    public ResponseCode getResponse() {
        return response;
    }
}
