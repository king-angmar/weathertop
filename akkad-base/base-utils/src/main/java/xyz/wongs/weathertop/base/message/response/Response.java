package xyz.wongs.weathertop.base.message.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Response<T> implements Serializable {
    private static final long serialVersionUID = -4505655308965878999L;

    //请求成功返回码为：0000
    private static final Integer SUCCESS_CODE = 0;
    //返回数据
    private T data;
    //返回码
    private int code=200;
    //返回描述
    private String msg;

    private boolean status=true;

    public Response() {
        this.code = SUCCESS_CODE;
        this.msg = "REQUEST SUCCESS";
    }

    public Response(int code, String msg) {
        this();
        this.code = code;
        this.msg = msg;
    }

    public Response(boolean status,int code, String msg) {
        this();
        this.status=status;
        this.code = code;
        this.msg = msg;
    }

    public Response(int code, String msg, T data) {
        this();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Response(boolean status,int code, String msg, T data) {
        this();
        this.status=status;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Response(T data) {
        this();
        this.data = data;
    }

}
