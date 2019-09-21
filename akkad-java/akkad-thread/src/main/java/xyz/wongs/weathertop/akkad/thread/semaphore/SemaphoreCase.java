package xyz.wongs.weathertop.akkad.thread.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SemaphoreCase {

    public static void main(String[] args) {
        SemaphoreService semaphoreService = new SemaphoreService();
        SemaphoreService3 semaphoreService3 = new SemaphoreService3();

        ExecutorService exec = Executors.newFixedThreadPool(10);

        for(int i=0;i<10;i++){
            SemaphoreRunnable semaphoreRunnable = new SemaphoreRunnable(" Thread Num "+i, semaphoreService3);
            exec.execute(semaphoreRunnable);
            System.out.println("可用通路数：" + semaphoreService.availablePermits());
        }

    }
}
