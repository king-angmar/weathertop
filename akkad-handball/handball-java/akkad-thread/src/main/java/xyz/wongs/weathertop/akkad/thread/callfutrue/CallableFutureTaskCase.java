package xyz.wongs.weathertop.akkad.thread.callfutrue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.wongs.weathertop.akkad.thread.SysUser;
import xyz.wongs.weathertop.akkad.thread.UserTask;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Component
public class CallableFutureTaskCase {

    public static void main(String[] args) {
        long beginTime = System.currentTimeMillis();
        //第一种方式
        ExecutorService executor = Executors.newFixedThreadPool(10);

        FutureTask<List<SysUser>> futureTask1 = new FutureTask<List<SysUser>>(new UserTask());
        FutureTask<List<SysUser>> futureTask2 = new FutureTask<List<SysUser>>(new UserTask());
        FutureTask<List<SysUser>> futureTask3 = new FutureTask<List<SysUser>>(new UserTask());
        FutureTask<List<SysUser>> futureTask4 = new FutureTask<List<SysUser>>(new UserTask());
        executor.submit(futureTask1);
        executor.submit(futureTask2);
        executor.submit(futureTask3);
        executor.submit(futureTask4);
        executor.shutdown();

        System.out.println("主线程在执行任务");
        try {
            int size = futureTask1.get().size()+futureTask2.get().size()+futureTask3.get().size()+futureTask4.get().size();
            long endTime = System.currentTimeMillis();
            System.out.println("task运行结果 "+(size) + " 耗时 "+ (endTime-beginTime) +" 毫秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");
    }
}
