package xyz.wongs.weathertop.akkad.thread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ClassName ReentrantLockCase
 * @Description
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2019/7/23 22:26
 * @Version 1.0.0
*/
public class ReentrantLockCase {

    public static void main(String[] args) throws InterruptedException{


        ReentrantLockCase reentrantLockCase = new ReentrantLockCase();

        reentrantLockCase.lockDowngrade_2();

    }


    /** ReentrantReadWriteLock支持锁降级
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/7/23 22:35
     * @param
     * @return void
     * @throws
     * @since
     */
    public void lockDowngrade_2(){
        ReentrantReadWriteLock rtLock = new ReentrantReadWriteLock();

        rtLock.writeLock().lock();
        System.out.println("get readLock.");

        rtLock.readLock().lock();
        System.out.println("blocking");
        rtLock.readLock().unlock();
        rtLock.writeLock().unlock();
    }


    /** 锁升级，ReentrantReadWriteLock是不支持的
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/7/23 22:35
     * @param
     * @return void
     * @throws
     * @since
     */
    public void lockDowngrade_1(){

        ReentrantReadWriteLock rtLock = new ReentrantReadWriteLock();
        rtLock.readLock().lock();
        System.out.println("get readLock.");
        rtLock.writeLock().lock();
        System.out.println("blocking");
    }

    /** 可重入锁，可以调用自己本类synchornized 方法以及父类中的synchornized方法
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/7/23 22:30
     * @param null
     * @return 
     * @throws 
     * @since 
    */
    public void reentrantLock() throws InterruptedException{
        final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.writeLock().lock();
                System.out.println("Thread real execute");
                lock.writeLock().unlock();
            }
        });

        lock.writeLock().lock();
        lock.writeLock().lock();
        t.start();
        Thread.sleep(200);
        System.out.println("realse one once");
        lock.writeLock().unlock();
        //一个线程获取多少次锁，就必须释放多少次锁。这对于内置锁也是适用的，
        // 每一次进入和离开synchornized方法(代码块)，就是一次完整的锁获取和释放
        lock.writeLock().unlock();
    }

}
