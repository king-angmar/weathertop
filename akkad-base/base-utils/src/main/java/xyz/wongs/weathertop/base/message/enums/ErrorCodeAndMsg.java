package xyz.wongs.weathertop.base.message.enums;

public enum ErrorCodeAndMsg {

    Data_number_does_not_exist("0001", "数据不存在"),
    Insufficient_data_number("0002", "数据不符合规范"),
    Data_number_is_empty("0003", "数据为空"),
    Network_error("9999", "网络错误，待会重试"),;

    private String code;
    private String msg;

    ErrorCodeAndMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
