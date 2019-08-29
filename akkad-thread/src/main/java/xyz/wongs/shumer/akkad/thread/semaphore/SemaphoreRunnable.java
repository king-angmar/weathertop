package xyz.wongs.shumer.akkad.thread.semaphore;

public class SemaphoreRunnable implements Runnable {


    String methName;

    SemaphoreService semaphoreService;


    public SemaphoreRunnable() {
    }
    public SemaphoreRunnable(String methName,SemaphoreService semaphoreService) {
        this.methName = methName;
        this.semaphoreService = semaphoreService;
    }

    @Override
    public void run() {
        semaphoreService.doSomething();
    }
}
