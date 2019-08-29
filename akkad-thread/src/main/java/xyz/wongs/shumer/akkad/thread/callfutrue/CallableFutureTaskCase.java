package xyz.wongs.shumer.akkad.thread.callfutrue;

import xyz.wongs.shumer.akkad.thread.SysUser;
import xyz.wongs.shumer.akkad.thread.UserTask;

import java.util.List;
import java.util.concurrent.*;

public class CallableFutureTaskCase {

    public static void main(String[] args) {
        //第一种方式
        ExecutorService executor = Executors.newFixedThreadPool(10);

        UserTask userTask = new UserTask();
        FutureTask<List<SysUser>> futureTask = new FutureTask<List<SysUser>>(userTask);
        executor.submit(futureTask);
        executor.shutdown();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        System.out.println("主线程在执行任务");
        try {
            System.out.println("task运行结果"+futureTask.get().size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");
    }
}
