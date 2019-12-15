package xyz.wongs.weathertop.base.web;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import xyz.wongs.weathertop.base.message.response.ResponseResult;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 *  ┏┓　　　┏┓
 *┏┛┻━━━┛┻┓
 *┃　　　　　　　┃ 　
 *┃　　　━　　　┃
 *┃　┳┛　┗┳　┃
 *┃　　　　　　　┃
 *┃　　　┻　　　┃
 *┃　　　　　　　┃
 *┗━┓　　　┏━┛
 *　　┃　　　┃神兽保佑
 *　　┃　　　┃代码无BUG！
 *　　┃　　　┗━━━┓
 *　　┃　　　　　　　┣┓
 *　　┃　　　　　　　┏┛
 *　　┗┓┓┏━┳┓┏┛
 *　　　┃┫┫　┃┫┫
 *　　　┗┻┛　┗┻┛
 * @ClassName BaseController
 * @Description 控制器支持类
 * @author WCNGS@QQ.COM
 * @date 2019/7/1 15:41
 * @Version 1.0.0
*/
public abstract class BaseController {

    @ExceptionHandler({BindException.class, ConstraintViolationException.class, ValidationException.class})
    public String bindException() {
        return "error/400";
    }

    /**
     * @Description 初始化统一返回信息
     * @param
     * @return cn.ffcs.np.common.util.response.ResponseResult
     * @throws
     * @date 2019/11/7 9:58
     */
    public ResponseResult getResponseResult() {
        return new ResponseResult();
    }

}
