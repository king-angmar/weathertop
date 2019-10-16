package xyz.wongs.weathertop.base.utils;

import com.xiaoleilu.hutool.io.FileUtil;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

/**
 *  ┏┓　　　┏┓
 *┏┛┻━━━┛┻┓
 *┃　　　　　　　┃ 　
 *┃　　　━　　　┃
 *┃　┳┛　┗┳　┃
 *┃　　　　　　　┃
 *┃　　　┻　　　┃
 *┃　　　　　　　┃
 *┗━┓　　　┏━┛
 *　　┃　　　┃神兽保佑
 *　　┃　　　┃代码无BUG！
 *　　┃　　　┗━━━┓
 *　　┃　　　　　　　┣┓
 *　　┃　　　　　　　┏┛
 *　　┗┓┓┏━┳┓┏┛
 *　　　┃┫┫　┃┫┫
 *　　　┗┻┛　┗┻┛
 * @ClassName NpFileUtil
 * @Description 
 * @author WCNGS@QQ.COM
 * @date 2018/8/31 16:37
 * @Version 1.0.0
*/
public class NpFileUtil extends FileUtil {

    private static Logger log = LoggerFactory.getLogger(NpFileUtil.class);

    /**
     * NIO way
     */
    public static byte[] toByteArray(String filename) {

        File f = new File(filename);
        if (!f.exists()) {
            log.error("文件未找到！" + filename);
            throw new RuntimeException("文件未找到");
        }
        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
            throw new RuntimeException("");
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                throw new RuntimeException("文件读取失败");
            }
            try {
                fs.close();
            } catch (IOException e) {
                throw new RuntimeException("文件读取失败");
            }
        }
    }

    /** 构建目录
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/8/1 17:29
     * @param outputDir
     * @param subDir
     * @return void
     * @throws
     * @since
     */
    public static void createDirectory(String outputDir,String subDir){

        File file = new File(outputDir);
        //子目录不为空
        if(!(subDir == null || subDir.trim().equals(""))){
            file = new File(outputDir + "/" + subDir);
        }
        if(!file.exists()){
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            file.mkdirs();
        }
    }

    /**解压tar.gz 文件
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/8/1 17:27
     * @param file 要解压的tar.gz文件对象
     * @param outputDir 要解压到某个指定的目录下
     * @return void
     * @throws IOException
     * @since
     */
    public static void unTarGz(File file,String outputDir) throws IOException{

        TarInputStream tarIn = null;
        try{
            tarIn = new TarInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(file))),1024 * 2);
            NpFileUtil.createDirectory(outputDir,null);
            TarEntry entry = null;
            while( (entry = tarIn.getNextEntry()) != null ){
                //是目录
                if(entry.isDirectory()){
                    entry.getName();
                    NpFileUtil.createDirectory(outputDir,entry.getName());
                } else{
                    //是文件
                    File tmpFile = new File(outputDir + "/" + entry.getName());
                    NpFileUtil.createDirectory(tmpFile.getParent() + "/",null);
                    OutputStream out = null;
                    try{
                        out = new FileOutputStream(tmpFile);
                        int length = 0;
                        byte[] b = new byte[2048];
                        while((length = tarIn.read(b)) != -1){
                            out.write(b, 0, length);
                        }
                    } catch(IOException ex){
                        throw ex;
                    } finally{
                        if(out!=null)
                            out.close();
                    }
                }
            }
        } catch(IOException ex){
            throw new IOException("解压归档文件出现异常",ex);
        } finally{
            try{
                if(tarIn != null){
                    tarIn.close();
                }
            }catch(IOException ex){
                throw new IOException("关闭tarFile出现异常",ex);
            }
        }
    }

    /**
     * 解压对.tar.gz文件至 .tar文件
     * 说明:我们一般都是对.tar.gz文件进行gzip解压; 进而获得形如.tar文件;再进行解压
     * 注:这里暂时不再深入学习,以后有闲暇时间可深入了解学习
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/8/2 9:41
     * @param compressedFilePath    要被解压的压缩文件 全路径
     * @param resultDirPath 解压文件存放绝对路径(目录)
     * @return boolean
     * @throws
     * @since
     */
    public static boolean gzipDecompression(String compressedFilePath, String resultDirPath) throws Exception {
        InputStream fin = null;
        BufferedInputStream in = null;
        OutputStream out = null;
        GzipCompressorInputStream gcis = null;
        try {
            out = Files.newOutputStream(Paths.get(resultDirPath));
            fin = Files.newInputStream(Paths.get(compressedFilePath));
            in = new BufferedInputStream(fin);
            gcis = new GzipCompressorInputStream(in);
            final byte[] buffer = new byte[1024];
            int n = 0;
            while (-1 != (n = gcis.read(buffer))) {
                out.write(buffer, 0, n);
            }
        } finally {
            if(gcis != null)
                gcis.close();
            if(in != null)
                in.close();
            if(fin != null)
                fin.close();
            if(out != null)
                out.close();
        }
        return true;
    }


}