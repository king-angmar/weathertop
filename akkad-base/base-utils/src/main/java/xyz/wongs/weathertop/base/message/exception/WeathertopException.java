package xyz.wongs.weathertop.base.message.exception;

import xyz.wongs.weathertop.base.message.enums.ErrorCodeAndMsg;


/**
 * @ClassName WeathertopException
 * @Description 
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2019/9/21 10:07
 * @Version 1.0.0
*/
public class WeathertopException extends RuntimeException{

    private static final long serialVersionUID = -6370612186038915645L;

    private final ErrorCodeAndMsg response;

    public WeathertopException(ErrorCodeAndMsg response) {
        this.response = response;
    }
    public ErrorCodeAndMsg getResponse() {
        return response;
    }
}
