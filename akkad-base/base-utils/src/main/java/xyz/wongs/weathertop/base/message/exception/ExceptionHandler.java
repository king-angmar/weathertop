package xyz.wongs.weathertop.base.message.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.wongs.weathertop.base.message.enums.ResponseCode;
import xyz.wongs.weathertop.base.message.response.Response;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName ExceptionHandler
 * @Description 全局异常处理Handler
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2019/9/23 15:03
 * @Version 1.0.0
*/
@ControllerAdvice
@Slf4j
public class ExceptionHandler {



    /** 自定义异常
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/9/23 17:53
     * @param request
     * @param ex
     * @return xyz.wongs.weathertop.base.message.response.Response
     * @throws
     * @since
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(WeathertopRuntimeException.class)
    @ResponseBody
    public Response handleWeathertopException(HttpServletRequest request, WeathertopRuntimeException ex) {
        log.error("WeathertopRuntimeException code:{},msg:{}",ex.getResponse().getCode(),ex.getResponse().getMsg());
        Response response = new Response(false,ex.getResponse().getCode(),ex.getResponse().getMsg());
        return response;
    }

    /**全局异常
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/9/23 17:53
     * @param request
     * @param ex
     * @return xyz.wongs.weathertop.base.message.response.Response
     * @throws
     * @since
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(GlobalException.class)
    @ResponseBody
    public Response handleException(HttpServletRequest request, Exception ex) {
        log.error("exception error:{}",ex);
        Response response = new Response();
        if (ex instanceof org.springframework.web.servlet.NoHandlerFoundException) {
            response.setCode(404);

        } else {
            response.setCode(500);
        }
        response.setMsg(ex.getMessage());
        response.setData(null);
        response.setStatus(false);
        return response;
    }

    /** 数据库中已存在异常
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/9/23 17:53
     * @param request
     * @param ex
     * @return xyz.wongs.weathertop.base.message.response.Response
     * @throws
     * @since
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    public Response handleDuplicateKeyException(HttpServletRequest request, DuplicateKeyException ex){
        log.error("exception error:{}", ex);
        Response response = new Response(false,ResponseCode.DUPLICATEKEY_ERROR_CODE.getCode(),ResponseCode.DUPLICATEKEY_ERROR_CODE.getMsg());
        return response;
    }

}
