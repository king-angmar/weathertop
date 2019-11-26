package xyz.wongs.weathertop.akkad.thread.semaphore;

import java.util.concurrent.Semaphore;

public class SemaphoreService2 extends SemaphoreService{


    // 同步关键类，构造方法传入的数字是多少，则同一个时刻，只运行多少个进程同时运行制定代码
    private Semaphore semaphore = new Semaphore(6);

    @Override
    public void doSomething() {
        try {
            //方法 acquire( int permits ) 参数作用，及动态添加 permits 许可数量,
            //semaphore.acquire(2) 表示每次线程进入将会占用2个通路，semaphore.release(2)
            //运行时表示归还2个通路。没有通路，则线程就无法进入代码块。
//            semaphore.acquire();
            semaphore.acquire(2);
            System.out.println(Thread.currentThread().getName() + ":doSomething start-" + getFormatTimeStr());
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + ":doSomething end-" + getFormatTimeStr());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release(2);
        }
    }


}
