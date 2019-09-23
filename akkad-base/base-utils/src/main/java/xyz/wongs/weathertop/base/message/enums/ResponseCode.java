package xyz.wongs.weathertop.base.message.enums;

public enum ResponseCode {

    PARAM_ERROR_CODE(400,"客户端请求的语法错误，服务器无法理解"),
    UNAUTHORIZED_ERROR_CODE(401,"请求要求用户的身份认证"),
    FORBIDDEN_ERROR_CODE(403,"服务器理解请求客户端的请求，但是拒绝执行此请求"),
    NOT_FOUND_ERROR_CODE(404,"服务器无法根据客户端的请求找到资源"),
    METHOD_NOT_ALLOWED_ERROR_CODE(405,"客户端请求中的方法被禁止"),
    NOT_ACCEPTABLE_ERROR_CODE(406,"服务器无法根据客户端请求的内容特性完成请求"),
    PROXY_AUTHENTICATION_REQUIRED_ERROR_CODE(407,"请求要求代理的身份认证，但请求者应当使用代理进行授权"),
    REQUEST_TIME_OUT_ERROR_CODE(408,"服务器等待客户端发送的请求时间过长，超时"),
    INTERNAL_SERVER_ERROR_CODE(500,"服务器内部错误，无法完成请求"),
    NOT_IMPLEMENTED_ERROR_CODE(501,"服务器不支持请求的功能，无法完成请求"),
    BAD_GATEWAY_ERROR_CODE(502,"网关或者代理服务器执行请求，但收到无效的响应"),
    SERVICE_UNAVAILABLE_ERROR_CODE(503,"由于超载或系统维护，服务器暂时的无法处理客户端的请求"),
    GATEWAY_TIME_OUT_ERROR_CODE(504,"充当网关或代理的服务器，未及时从远端服务器获取请求"),
    HTTP_VERSION_NOT_SUPPORTED_ERROR_CODE(505,"服务器不支持请求的HTTP协议的版本，无法完成处理"),


    RESOURCE_NOT_EXIST(1001, "资源不存在"),
    INSUFFICIENT_RESOURCE(1002, "资源不符合规范"),
    NETWORK_ERROR(9999, "网络错误，待会重试"),,
    DUPLICATEKEY_ERROR_CODE(1003,"数据库中已存在该记录");

    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
