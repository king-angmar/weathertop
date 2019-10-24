package xyz.wongs.weathertop.base.message.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.wongs.weathertop.base.message.enums.ResponseCode;
import xyz.wongs.weathertop.base.message.response.ResponseResult;
import xyz.wongs.weathertop.base.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.io.IOException;
import java.util.List;
import java.util.Set;

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
    public ResponseResult handleWeathertopException(HttpServletRequest request, WeathertopRuntimeException ex) {
        log.error("WeathertopRuntimeException code:{},msg:{}",ex.getResponse().getCode(),ex.getResponse().getMsg());
        ResponseResult response = new ResponseResult(false,ex.getResponse().getCode(),ex.getResponse().getMsg());
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
    public ResponseResult handleException(HttpServletRequest request, Exception ex) {
        log.error("exception error:{}",ex);
        ResponseResult response = new ResponseResult();
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
    public ResponseResult handleDuplicateKeyException(HttpServletRequest request, DuplicateKeyException ex){
        log.error("exception error:{}", ex);
        ResponseResult response = new ResponseResult(false,ResponseCode.DUPLICATEKEY_ERROR_CODE.getCode(),ResponseCode.DUPLICATEKEY_ERROR_CODE.getMsg());
        return response;
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public ResponseResult RuntimeException(RuntimeException ex){
        log.error(ex.getMessage(),ex);
        ResponseResult response = new ResponseResult(false,ResponseCode.ERROR_RUNTION.getCode(),ResponseCode.ERROR_RUNTION.getMsg());
        return response;
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseResult exception(Exception ex){
        log.error(ex.getMessage(),ex);
        ResponseResult response = new ResponseResult(false,ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getMsg());
        return response;
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseResult httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex){
        log.error(ex.getMessage(),ex);
        ResponseResult response = new ResponseResult(false,ResponseCode.ERROR_MOTHODNOTSUPPORT.getCode(),ResponseCode.ERROR_MOTHODNOTSUPPORT.getMsg());
        return response;
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(IOException.class)
    public ResponseResult iOException(IOException ex){
        log.error(ex.getMessage(),ex);
        ResponseResult response = new ResponseResult(false,ResponseCode.ERROR_IO.getCode(),ResponseCode.ERROR_IO.getMsg());
        return response;
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(NullPointerException.class)
    public ResponseResult nullPointer(NullPointerException ex){
        log.error(ex.getMessage(),ex);
        ResponseResult response = new ResponseResult(false,ResponseCode.ERROR_NULL.getCode(),ResponseCode.ERROR_NULL.getMsg());
        return response;
    }

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(ClassCastException.class)
    public ResponseResult classCastException(ClassCastException ex){
        log.error(ex.getMessage(),ex);
        ResponseResult response = new ResponseResult(false,ResponseCode.ERROR_CLASS_CAST.getCode(),ResponseCode.ERROR_CLASS_CAST.getMsg());
        return response;
    }

    /**统一处理请求参数校验.
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/23 9:22
     * @param e
     * @return java.lang.String
     * @throws
     * @since
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseResult handleConstrainViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
            message.append(pathArr[1]).append(violation.getMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        ResponseResult response = new ResponseResult(false,ResponseCode.VALID_ENTITY_PARAMS.getCode(),message.toString());
        return response;
    }

    /** 统一请求参数校验(实体对象传参).
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/10/23 9:22
     * @param e
     * @return java.lang.String
     * @throws
     * @since
     */
    @org.springframework.web.bind.annotation.ExceptionHandler({BindException.class})
    public ResponseResult validExceptionHandler(BindException e) {
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        ResponseResult response = new ResponseResult(false,ResponseCode.VALID_UNION_PARAMS.getCode(),message.toString());
        return response;
    }
}
