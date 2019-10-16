//package cn.ffcs.np.analydb.concurrent;
//
//import cn.ffcs.np.analydb.entity.CsmsNpdata;
//import cn.ffcs.np.analydb.service.CsmsNpdataService;
//import cn.ffcs.np.common.TaskRuntimeException;
//import cn.ffcs.np.common.util.FileDocUtil;
//import cn.ffcs.np.common.util.NpFileUtil;
//import cn.ffcs.np.utils.FileDocTaskUtil;
//import cn.ffcs.np.utils.SpringUtil;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.*;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//@Slf4j
//public class TaskThread implements Runnable{
//
//
//
//    /**
//     * 分片数量
//     */
//    private static final String MOD = "8";
//    private static final BigInteger m2 = new BigInteger(MOD);
//
//    public static final String ENCODING="utf8";
//
//    private File file;
//
//    public TaskThread(){}
//
//    public TaskThread(File file) {
//        this.file = file;
//    }
//
//    @Override
//    public void run() {
//        boolean flag = insertDb(file,CsmsNpdata.class.getName());
//        if(!flag){
//            log.error(Thread.currentThread().getName()+ "执行失败 失败文件名 "+ file.getName());
//            throw new TaskRuntimeException("Parsing an inbound exception "+ file.getName());
//        }
//        log.error(Thread.currentThread().getName()+ "执行成功 成功文件名 "+ file.getName());
//        NpFileUtil.del(file);
//    }
//
//
//    /** 解析文件入库入口
//     * @author WCNGS@QQ.COM
//     * @See
//     * @date 2019/8/1 19:57
//     * @param file 文件
//     * @param clazzName 类名称
//     * @return boolean 根据前后执行结果比对，成功 返回TRUE ,否则 FALSE
//     * @throws
//     * @since
//     */
//    public boolean insertDb(File file,String clazzName) {
//        /**
//         * 每次处理多少数量
//         */
//        int size =4;
//
//        int record=0;
//        int tempRecord=0;
//        FileInputStream inputStream = null;
//        BufferedReader br = null;
//        try {
//            inputStream = new FileInputStream(file);
//            br = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
//            List<CsmsNpdata> list = new ArrayList<>(size);
//            int it=0;
//            //判断是否是最后一行
//            String str = null;
//            boolean flag = true;
//            while((str=br.readLine())!=null) {
//                str = str.trim();
//                if(flag){
//                    tempRecord = Integer.parseInt(str);
//                    log.error(file.getName()+" 文件总数量为 "+ tempRecord);
//                    flag=false;
//                    continue;
//                }
//
//                CsmsNpdata bean = FileDocTaskUtil.getBeanInstance(clazzName);
//                FileDocUtil.compBean(bean,str,true,",");
//                if(null==bean){
//                    continue;
//                }
////                log.error(Thread.currentThread().getName()+" 实体Bean Value is "+ bean.toString());
//                it++;
//                list.add(bean);
//                if(it==size){
////                    log.error(Thread.currentThread().getName()+" 准备提交");
//                    it=0;
//                    record+=size;
////                    paginationDb(list);
//                }
//            }
//            if(!list.isEmpty()){
////                log.error(Thread.currentThread().getName()+" 二次提交");
//                record+=list.size();
////                paginationDb(list);
//            }
//        } catch (IOException e) {
//            log.error(" IO 异常",e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error(" Exception ! Error Message is "+ e.getMessage());
//        } finally {
//            if(br!=null){
//                try {
//                    br.close();
//                    inputStream.close();
//                } catch (IOException e) {
//                    log.error(" Close BufferedReader InputStream failure");
//                }
//            }
//        }
//        log.warn(file.getName()+" 文件处理入库的数据 共计 "+ record +" 条");
//        return tempRecord==record?true:false;
//    }
//
//    /** 根据分片键与分片数量取模，指定分属不通Collection，再各自独立入库
//     * @author WCNGS@QQ.COM
//     * @See
//     * @date 2019/8/1 19:54
//     * @param list
//     * @return void
//     * @throws
//     * @since
//     */
//    protected void paginationDb(List<CsmsNpdata> list){
//
//        List listPartition1 = Collections.synchronizedList(new ArrayList(5));
//        List listPartition2 = Collections.synchronizedList(new ArrayList(5));
//        List listPartition3 = Collections.synchronizedList(new ArrayList(5));
//        List listPartition4 = Collections.synchronizedList(new ArrayList(5));
//        List listPartition5 = Collections.synchronizedList(new ArrayList(5));
//        List listPartition6 = Collections.synchronizedList(new ArrayList(5));
//        List listPartition7 = Collections.synchronizedList(new ArrayList(5));
//        List listPartition8 = Collections.synchronizedList(new ArrayList(5));
//
//        for(CsmsNpdata bean:list){
//            int condition = FileDocTaskUtil.routerRule(bean.getId(),m2);
//            switch (condition){
//                case(0):
//                    listPartition1.add(bean);
//                    break;
//                case(1):
//                    listPartition2.add(bean);
//                    break;
//                case(2):
//                    listPartition3.add(bean);
//                    break;
//                case(3):
//                    listPartition4.add(bean);
//                    break;
//                case(4):
//                    listPartition5.add(bean);
//                    break;
//                case(5):
//                    listPartition6.add(bean);
//                    break;
//                case(6):
//                    listPartition7.add(bean);
//                    break;
//                case(7):
//                    listPartition8.add(bean);
//                    break;
//                default:
//                    throw new TaskRuntimeException(" 没有对应的分片键 MOD Values is "+ condition);
//            }
//
//            checkSize4Commit(listPartition1);
//            checkSize4Commit(listPartition2);
//            checkSize4Commit(listPartition3);
//            checkSize4Commit(listPartition4);
//            checkSize4Commit(listPartition5);
//            checkSize4Commit(listPartition6);
//            checkSize4Commit(listPartition7);
//            checkSize4Commit(listPartition8);
//        }
//
//        checkEmpty4Commit(listPartition1);
//        checkEmpty4Commit(listPartition2);
//        checkEmpty4Commit(listPartition3);
//        checkEmpty4Commit(listPartition4);
//        checkEmpty4Commit(listPartition5);
//        checkEmpty4Commit(listPartition6);
//        checkEmpty4Commit(listPartition7);
//        checkEmpty4Commit(listPartition8);
//
//        list.clear();
//    }
//
//
//    /** 判断List集合是否为空，不为空插入数据
//     * @author WCNGS@QQ.COM
//     * @See
//     * @date 2019/8/1 18:49
//     * @param list
//     * @return void
//     * @throws
//     * @since
//     */
//    protected void checkEmpty4Commit(List<CsmsNpdata> list){
//        if(!list.isEmpty()){
//            submitInsert(list);
//        }
//    }
//
//    /** 判断List大小是否到达规定阈值，不为空插入数据
//     * @author WCNGS@QQ.COM
//     * @See
//     * @date 2019/8/1 17:24
//     * @param list
//     * @return void
//     * @throws
//     * @since
//     */
//    protected void checkSize4Commit(List<CsmsNpdata> list){
//        if(list.size()==5){
//            submitInsert(list);
//        }
//    }
//
//    protected void submitInsert(List<CsmsNpdata> list){
////        csmsNpdataService.insertBatchByOn(list);
//        list.clear();
//    }
//
//    private CsmsNpdataService csmsNpdataService = SpringUtil.getBean(CsmsNpdataService.class);
//}
