package xyz.wongs.weathertop.base.message.exception;

public class GlobalException extends Exception {

    private int code;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public GlobalException() {

    }

    public GlobalException(String message) {
        super(message);
    }
    public GlobalException(String message, int code) {
        super(message);
        this.code = code;
    }
}
