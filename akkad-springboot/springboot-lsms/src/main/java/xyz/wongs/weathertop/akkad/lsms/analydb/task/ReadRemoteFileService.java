package xyz.wongs.weathertop.akkad.lsms.analydb.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.wongs.weathertop.akkad.lsms.analydb.entity.SmNpdb;
import xyz.wongs.weathertop.akkad.lsms.analydb.service.SmNpdbService;
import xyz.wongs.weathertop.akkad.lsms.common.TaskRuntimeException;
import xyz.wongs.weathertop.akkad.lsms.utils.FileDocTaskUtil;
import xyz.wongs.weathertop.base.utils.FileDocUtil;
import xyz.wongs.weathertop.base.utils.NpFileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class ReadRemoteFileService {

    public static final String ENCODING = "utf8";
    private static final int PARTION_SIZE = 400;

    @Autowired
    private SmNpdbService smNpdbService;
    /**
     * 每次处理多少数量
     */
    private int size = 4000;

    /**
     * 解析文件入库入口
     *
     * @param file      文件
     * @param clazzName 类名称
     * @return boolean 根据前后执行结果比对，成功 返回TRUE ,否则 FALSE
     * @throws
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/8/1 19:57
     * @since
     */
    public boolean insertDb(File file, String clazzName) {

        int record = 0;
        int tempRecord = 0;
        FileInputStream inputStream = null;
        BufferedReader br = null;
        try {
            inputStream = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
            List<SmNpdb> insertList = new ArrayList<SmNpdb>(size);

            int it = 0;
            //判断是否是最后一行
            String str = null;
            boolean flag = true;
            while ((str = br.readLine()) != null) {
                str = str.trim();
                if (flag) {
                    //文件的第一行为包含的总记录数
                    tempRecord = Integer.parseInt(str);
                    log.info("[{}]包含总数据量为[{}]", file.getName(), tempRecord);
                    flag = false;
                    continue;
                }
                SmNpdb bean = FileDocTaskUtil.getBeanInstance(clazzName);
                FileDocUtil.compBean(bean, str, true, ",");
                it++;
                insertList.add(bean);

                if (it == size) {
                    it = 0;
                    record = record + size;
                    submitInsert(insertList);
                }
            }
            if (!insertList.isEmpty()) {
                record = record + insertList.size();
                submitInsert(insertList);
            }
        } catch (IOException e) {
            log.error(" IO 异常: ", e.getMessage());
        } catch (ClassNotFoundException e) {
            log.error(" ClassNotFoundException 异常: " + e.getMessage());
        } catch (Exception e) {
            log.error(" Exception ! Error Message is: " + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                    inputStream.close();
                } catch (IOException e) {
                    log.error(" Close BufferedReader InputStream failure");
                }
            }
        }
        log.warn(" 处理入库的数据 共计 " + record + " 条");
        return tempRecord == record;
    }


    /**
     * 判断List集合是否为空，不为空插入数据
     *
     * @param list
     * @return void
     * @throws
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/8/1 18:49
     * @since
     */
    protected void checkEmpty4Commit(List<SmNpdb> list) {
        if (!list.isEmpty()) {
            submitInsert(list);
        }
    }

    /**
     * 判断List大小是否到达规定阈值，不为空插入数据
     *
     * @param list
     * @return void
     * @throws
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/8/1 17:24
     * @since
     */
    protected void checkSize4Commit(List<SmNpdb> list) {
        if (list.size() == PARTION_SIZE) {
            submitInsert(list);
        }
    }

    protected void submitInsert(List<SmNpdb> list) {
        smNpdbService.insertBatchByOn(list);
        list.clear();
    }

    /**
     * @param localDirectoryFile
     * @return void
     * @throws
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/8/1 17:30
     * @since
     */
    public void getFileList(File localDirectoryFile) throws IOException {
        if (localDirectoryFile.isDirectory()) {
            File[] files = localDirectoryFile.listFiles();
            for (File file : files) {
                getFileList(file);
            }
        } else {
            checkFileList(localDirectoryFile);
        }
    }

    /**
     * 检查对文件列表处理结果
     *
     * @param file
     * @return void
     * @throws
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/8/2 10:23
     * @since
     */
    public void checkFileList(File file) {
        boolean flag = insertDb(file, SmNpdb.class.getName());
        if (!flag) {
            log.error(" 文件解析入库数量前后不一致 "+file.getName());
            throw new TaskRuntimeException("Parsing an inbound exception " + file.getName());
        }
        NpFileUtil.del(file);
    }
}
