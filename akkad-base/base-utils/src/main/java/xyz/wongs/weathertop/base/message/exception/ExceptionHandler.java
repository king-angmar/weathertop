package xyz.wongs.weathertop.base.message.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.wongs.weathertop.base.message.enums.ErrorCodeAndMsg;
import xyz.wongs.weathertop.base.message.response.Response;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(WeathertopException.class)
    @ResponseBody
    public Response handleStudentException(HttpServletRequest request, WeathertopException ex) {
        log.error("WeathertopException code:{},msg:{}",ex.getResponse().getCode(),ex.getResponse().getMsg());
        Response response = new Response(ex.getResponse().getCode(),ex.getResponse().getMsg());
        return response;
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handleException(HttpServletRequest request, Exception ex) {
        log.error("exception error:{}",ex);
        Response response = new Response(ErrorCodeAndMsg.Network_error.getCode(),
                ErrorCodeAndMsg.Network_error.getMsg());
        return response;
    }

}
