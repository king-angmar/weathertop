package xyz.wongs.weathertop.war3.web.zonecode.exception;

import java.io.PrintWriter;

/**
 * @author WCNGS@QQ.CO
 * @version V1.0
 * @Title:
 * @Package spring-cloud xyz.wongs.basic.common.exception
 * @Description: TODO
 * @date 2018/7/3 15:20
 **/
public class NoIpAddressException extends Exception {

    public NoIpAddressException() {
        super();
    }

    public NoIpAddressException(String message) {
        super(message);
    }
    @Override
    public void printStackTrace() {
        // TODO Auto-generated method stub
        super.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        // TODO Auto-generated method stub
        super.printStackTrace(s);
    }
}
