package xyz.wongs.weathertop.base;

import java.io.Serializable;

/**
 * 统一返回bean
 *
 * @param <T>
 * @author Lin
 * @date 2018/08/23
 */
public class ResultBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int SUCCESS = 0;
    private static final int FAIL = 1;
    private String msg = "success";
    private int code = SUCCESS;
    private T data;

    public ResultBean() {
        super();
    }

    public ResultBean(T data) {
        super();
        this.data = data;
    }

    public ResultBean(String msg, T data) {
        super();
        this.msg = msg;
        this.data = data;
    }

    public ResultBean(Throwable e) {
        super();
        this.msg = e.toString();
        this.code = FAIL;
    }

    public ResultBean(String msg, Throwable e) {
        super();
        this.msg = msg;
        this.code = FAIL;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

}
