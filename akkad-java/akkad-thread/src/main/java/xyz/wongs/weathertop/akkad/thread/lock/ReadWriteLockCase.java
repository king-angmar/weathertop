package xyz.wongs.weathertop.akkad.thread.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReadWriteLockCase {

    public static void main(String[] args) throws Exception{


        ExecutorService cachedThreadPool = Executors.newScheduledThreadPool(100);

        CacheLock cacheLock = new CacheLock();

        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ReadWriteFile.readFile(Thread.currentThread());
            }
        });
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ReadWriteFile.writeFile(Thread.currentThread());
            }
        });

//        for(int i=0;i<20;i++) {
//            final int index = 0;
//            Thread.sleep(index * 1000);
//            cachedThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    cacheLock.put("KEY",index);
//                }
//            });
//
//            cachedThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println(" Get "+Thread.currentThread().getName()+" value is "+cacheLock.get("KEY"));
//                }
//            });
//
//        }
        cachedThreadPool.shutdown();
    }


}
