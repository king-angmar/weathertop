package xyz.wongs.weathertop.akkad.thread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteFile {

    public static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    public static void readFile(Thread thread) {
        lock.readLock().lock();

        try {
            if(!lock.isWriteLocked()){
                System.out.println("当前为只读锁");
            }
            for (int i = 0; i < 5; i++) {
                Thread.sleep(20);
                System.out.println(thread.getName() + ":正在进行读操作……");
            }

            System.out.println(thread.getName() + ":读操作完毕！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("释放读锁！");
            lock.readLock().unlock();
        }
    }


    public static void writeFile(Thread thread) {
        lock.writeLock().lock();
        try {
            if(lock.isWriteLocked()){
                System.out.println("当前为写锁");
            }
            for (int i = 0; i < 5; i++) {
                Thread.sleep(20);
                System.out.println(thread.getName() + ":正在进行写操作……");
            }

            System.out.println(thread.getName() + ":写操作完毕！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("释放写锁！");
            lock.writeLock().unlock();
        }
    }
}
