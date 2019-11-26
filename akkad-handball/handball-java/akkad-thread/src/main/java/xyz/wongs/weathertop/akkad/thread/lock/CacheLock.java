package xyz.wongs.weathertop.akkad.thread.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheLock {

    //利用hashmap作为底层数据结构
    private Map<String, Object> cache=new HashMap<String, Object>();
    //构造读写锁
    private ReentrantReadWriteLock readwritelock=new ReentrantReadWriteLock();
    //读锁
    private Lock readLock=readwritelock.readLock();
    //写锁
    private Lock writeLock=readwritelock.writeLock();

    public void put(String key,Object obj){
        writeLock.lock();
        try {
            boolean readLock = readwritelock.isWriteLocked();
            if (readLock) {
                System.out.println("############ Write 线程 "+Thread.currentThread().getName()+"当前为写锁！");
            }
            System.out.println(" 正在写 值 是 "+obj);
            cache.put(key,obj);
            System.out.println(Thread.currentThread().getName() + ":写操作完毕！");
        } finally {
            System.out.println("############ Write 线程 "+Thread.currentThread().getName()+"释放写锁！");
            writeLock.unlock();
        }
    }


    public Object get(String key){
        readLock.lock();
        try {
            boolean readLock = readwritelock.isWriteLocked();
            if (!readLock) {
                System.out.println("当前为读锁！");
            }
            //System.out.println(" Read 线程 "+Thread.currentThread().getName()+" 正在读");
            return cache.get(key);
        } finally {
            System.out.println(" Read 线程 "+Thread.currentThread().getName()+"释放写锁！");
            readLock.unlock();
        }

    }

}
