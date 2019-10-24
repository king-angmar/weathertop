package xyz.wongs.weathertop;

import org.junit.Test;
import xyz.wongs.weathertop.base.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ShiroAppTest {



    private static ExecutorService executor = Executors.newFixedThreadPool(4);

    @Test
    public void shouldAnswerWithTrue() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = "2019-10-23 19:09:29";

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            executor.execute(new SubThread(sdf,dateString));
        }
    }

}

class SubThread implements Runnable {

    private SimpleDateFormat sdf;
    private String dateString;

    public SubThread(SimpleDateFormat sdf,String dateString){
        this.sdf=sdf;
        this.dateString=dateString;

    }

    @Override
    public void run() {
        String newDateString = "";
        try {
            Date dateRef = sdf.parse(dateString);
            newDateString = sdf.format(dateRef).toString();
            if(!newDateString.equals(dateString)) {
                System.out.println("ThreadName = " + Thread.currentThread().getName() + "出错  源日期字符串：" + dateString + "目标转换日期为：" + newDateString);
            }
        } catch (ParseException e) {
            System.out.println("ParseException ThreadName = " + Thread.currentThread().getName()+" ==> "+e.getMessage()+ "出错  源日期字符串：" + dateString + "目标转换日期为：" + newDateString);
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException ThreadName = " + Thread.currentThread().getName()+" ==> "+e.getMessage()+ "出错  源日期字符串：" + dateString + "目标转换日期为：" + newDateString);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException ThreadName = " + Thread.currentThread().getName()+" ==> "+e.getMessage()+ "出错  源日期字符串：" + dateString + "目标转换日期为：" + newDateString);
        }
    }


}
