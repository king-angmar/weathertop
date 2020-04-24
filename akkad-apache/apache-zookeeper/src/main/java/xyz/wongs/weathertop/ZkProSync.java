package xyz.wongs.weathertop;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class ZkProSync {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    private static Stat stat = new Stat();

    public static void main(String[] args) {
        String rootPath = "/abram";
        String connectString = "192.168.125.128:2181,192.168.125.128:2182,192.168.125.128:2183";
        int sessionTimeOut = 5000;
        try {
            zooKeeper = new ZooKeeper(connectString, sessionTimeOut, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if(Event.KeeperState.SyncConnected == event.getState()){
                        System.out.println(" Connect SUCCUES");
                        if(Event.EventType.None == event.getType() && null == event.getPath()){
                            countDownLatch.countDown();
                        } else if(event.getType() == Event.EventType.NodeDataChanged){
                            try {
                                System.out.println("数据已经变更, 当前值为: "+ new String(zooKeeper.getData(event.getPath(),true,stat)));
                            } catch (KeeperException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            countDownLatch.await();
            System.out.println(new String(zooKeeper.getData(rootPath,true,stat)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
