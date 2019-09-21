package xyz.wongs.weathertop.socket.server;

import lombok.extern.slf4j.Slf4j;
import xyz.wongs.weathertop.base.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SocketServer {

    public static void main(String[] args) {

        SocketServer socketServer = new SocketServer();

//        socketServer.initServer(8080,null);

        socketServer.initRunnableServer(8080,10,null);

    }


    public void initRunnableServer(int port,int poolSize,String charsetName){

        log.error(" BEGIN INIT SERVER");
        ServerSocket server = null;
        ExecutorService executorService = Executors.newScheduledThreadPool(poolSize);

        try {
            server = new ServerSocket(port);
            server.setSoTimeout(15);
            byte[] bytes = new byte[1024];
            while (true) {
                Socket socket = server.accept();
                Runnable runnable=()->{
                    InputStream inputStream = null;
                    try {
                        inputStream = socket.getInputStream();
                        int len;
                        StringBuilder sb = new StringBuilder();
                        while ((len = inputStream.read(bytes)) != -1) {
                            sb.append(new String(bytes, 0, len, SocketServer.getCharacter(charsetName)));
                        }
                        log.error(" Content is " + sb.toString());
                    } catch (IOException e) {
                        log.error(" INIT SERVER IS ERROR Exception "+ e.getMessage());
                    } finally {
                        try {
                            if(null!=inputStream)
                                inputStream.close();

                            if(null!=socket)
                                socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                executorService.submit(runnable);
            }
        } catch (IOException e) {
            log.error(" INIT SERVER IS ERROR Exception "+ e.getMessage());
        } finally {
            try {
                if(null!=server)
                    server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.error(" END INIT SERVER");
    }

    /**
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/9/4 13:43
     * @param port  端口
     * @param character 编码
     * @return void
     * @throws
     * @since
     */
    public void initServer(int port,String charsetName){

        log.error(" BEGIN INIT SERVER");
        Socket socket = null;
        ServerSocket server = null;
        InputStream inputStream = null;
        boolean flag = true;
        try {
            server = new ServerSocket(port);
            byte[] bytes = new byte[1024];
            while (flag) {
                socket = server.accept();
                inputStream = socket.getInputStream();
                int len;
                StringBuilder sb = new StringBuilder();
                while ((len = inputStream.read(bytes)) != -1) {
                    sb.append(new String(bytes, 0, len, SocketServer.getCharacter(charsetName)));
                }
                if(!StringUtils.isNotEmpty(sb.toString())){
                    continue;
                }
                log.error(" Content is " + sb.toString());
                if(sb.toString().equalsIgnoreCase(EXITS)){
                    flag = false;
                }
            }
        } catch (IOException e) {
            log.error(" INIT SERVER IS ERROR Exception "+ e.getMessage());
        } finally {
            try {
                if(null!=inputStream)
                    inputStream.close();

                if(null!=socket)
                    socket.close();

                if(null!=server)
                    server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.error(" END INIT SERVER");
    }

    public static String getCharacter(String charsetName){
        return null==charsetName?SocketServer.CHARACTER_UTF8:charsetName;
    }

    public final static String CHARACTER_UTF8="UTF-8";

    public final static String EXITS="exits";
}
