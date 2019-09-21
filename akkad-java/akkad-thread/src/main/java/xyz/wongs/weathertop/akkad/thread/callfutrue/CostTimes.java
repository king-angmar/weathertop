package xyz.wongs.weathertop.akkad.thread.callfutrue;

import xyz.wongs.weathertop.akkad.thread.SysUser;
import xyz.wongs.weathertop.akkad.thread.UserTask;

import java.util.List;

public class CostTimes {

    public static void main(String[] args) throws Exception{
        long beginTime = System.currentTimeMillis();
        UserTask userTask = new UserTask();

        List<SysUser> list1 = new UserTask().call();
        List<SysUser> list2 = new UserTask().call();
        List<SysUser> list3 = new UserTask().call();
        List<SysUser> list4 = new UserTask().call();

        System.out.println("主线程在执行任务");
        int size =list1.size() + list2.size() + list3.size() + list4.size();
        long endTime = System.currentTimeMillis();
        System.out.println("task运行结果" + (size) + " 耗时 " + (endTime - beginTime) + " 毫秒");

    }
}
