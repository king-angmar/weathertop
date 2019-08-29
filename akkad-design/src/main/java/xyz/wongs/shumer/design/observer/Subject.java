package xyz.wongs.shumer.design.observer;

public class Subject extends BaseSubject {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg){
        this.msg=msg;
        this.notifyObserver();
    }
}
