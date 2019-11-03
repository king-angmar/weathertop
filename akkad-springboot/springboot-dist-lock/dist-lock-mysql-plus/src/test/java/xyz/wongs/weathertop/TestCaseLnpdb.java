package xyz.wongs.weathertop;

import com.github.zheng93775.mlock.MLock;
import org.junit.Test;

import java.util.Random;

public class TestCaseLnpdb extends BaseUnit {

    private MLock mLock;

    @Test
    public void test() throws InterruptedException {
        mLock = new MLock("DailyJob");
        Thread thread1 = startThread(1);
        Thread thread2 = startThread(2);
        thread1.join();
        thread2.join();
    }

    private Thread startThread(int number) {
        Random random = new Random();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean lockResult = false;
                for (int i = 1; i < 10; i++) {
                    try {
                        System.out.println(Thread.currentThread().getName() +"==>"+ number + " Begin try lock");
                        lockResult = mLock.tryLock();
                        System.out.println(Thread.currentThread().getName()  +"==>"+ number + " 获取锁的结果是:  " + lockResult);
                        if (lockResult == false) {
                            int ms = random.nextInt(10000);
                            System.out.println(Thread.currentThread().getName()  +"==>" + number + " 并未获取到锁，开始休眠 " + ms + " ms");
                            Thread.sleep(ms);
                            continue;
                        }
                        int ms = random.nextInt(10000);
                        System.out.println(Thread.currentThread().getName()  +"==>" + number + " 开始工作 " + ms + " ms");
                        Thread.sleep(ms);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        mLock.unlock();
                        int ms = random.nextInt(5000);
                        System.out.println(Thread.currentThread().getName()  +"==>" + number + " 释放锁 " + ms + " ms");
                        try {
                            Thread.sleep(ms);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        thread.start();
        return thread;
    }
}
