//package xyz.wongs.weathertop.akkad.lsms.analydb.task;
//
//import xyz.wongs.weathertop.akkad.lsms.analydb.entity.CsmsNpdata;
//import xyz.wongs.weathertop.akkad.lsms.analydb.service.CsmsNpdataService;
//import xyz.wongs.weathertop.akkad.lsms.common.TaskRuntimeException;
//import cn.ffcs.np.common.util.FileDocUtil;
//import cn.ffcs.np.common.util.NpFileUtil;
//import xyz.wongs.weathertop.akkad.lsms.utils.FileDocTaskUtil;
//import xyz.wongs.weathertop.akkad.lsms.utils.InsertConst;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.*;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Slf4j
//@Component
//public class ReadRemoteFileTask {
//
//    public static final String ENCODING = "utf8";
//    /**
//     * 分片数量
//     */
//    private static final String MOD = "8";
//    private static final BigInteger m2 = new BigInteger(MOD);
//    private static final int PARTION_SIZE = 400;
//    /**
//     * 区分三种sql操作
//     **/
//    private static final String INSERT = "1";
//    private static final String UPDATE = "2";
//    private static final String DELETE = "0";
//    @Autowired
//    private CsmsNpdataService csmsNpdataService;
//    /**
//     * 每次处理多少数量
//     */
//    private int size = 4000;
//
//    /**
//     * 解析文件入库入口
//     *
//     * @param file      文件
//     * @param clazzName 类名称
//     * @return boolean 根据前后执行结果比对，成功 返回TRUE ,否则 FALSE
//     * @throws
//     * @author WCNGS@QQ.COM
//     * @See
//     * @date 2019/8/1 19:57
//     * @since
//     */
//    public boolean insertDb(File file, String clazzName) {
//
//        int record = 0;
//        int tempRecord = 0;
//        FileInputStream inputStream = null;
//        BufferedReader br = null;
//        try {
//            inputStream = new FileInputStream(file);
//            br = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
//
//            //区分当前操作是insert/update/delete
//            List<CsmsNpdata> insertList = new ArrayList<>(size);
//            List<CsmsNpdata> updateList = new ArrayList<>();
//            List<CsmsNpdata> deleteList = new ArrayList<>();
//
//            int it = 0;
//            //判断是否是最后一行
//            String str = null;
//            String actionType = null;
//            boolean flag = true;
//            String fileName = file.getName();
//            String npareaid = fileName.length() > 7 ? fileName.substring(4, 7) : "";
//            while ((str = br.readLine()) != null) {
//                str = str.trim();
//                if (flag) {
//                    //文件的第一行为包含的总记录数
//                    tempRecord = Integer.parseInt(str);
//                    log.warn("[{}]包含总数据量为[{}]", file.getName(), tempRecord);
//                    flag = false;
//                    continue;
//                }
//                //str = "1,18522235028,00350220,00240220,00300220,2019-05-01 09:00:00"
//                actionType = str.substring(0, str.indexOf(','));
//                String npCode = str.substring(str.indexOf(',') + 1);
//                CsmsNpdata bean = FileDocTaskUtil.getBeanInstance(clazzName);
//                FileDocUtil.compBean(bean, npCode, true, ",");
//                bean.setNpareaid(npareaid);
//                bean.setFileName(fileName);
//                it++;
//                switch (actionType) {
//                    case INSERT:
//                        insertList.add(bean);
//                        break;
//                    case UPDATE:
//                        updateList.add(bean);
//                        break;
//                    case DELETE:
//                        deleteList.add(bean);
//                        break;
//                    default:
//                        break;
//                }
//                if (it == size) {
//                    it = 0;
//                    record = record + size;
//                    paginationDb(insertList);
//                    updateDB(updateList);
//                    deleteDB(deleteList);
//                }
//            }
//            if (!insertList.isEmpty()) {
//                record = record + insertList.size();
//                paginationDb(insertList);
//            }
//            if (!updateList.isEmpty()) {
//                record = record + updateList.size();
//                updateDB(updateList);
//            }
//            if (!deleteList.isEmpty()) {
//                record = record + deleteList.size();
//                deleteDB(deleteList);
//            }
//        } catch (IOException e) {
//            log.error(" IO 异常: ", e.getMessage());
//        } catch (ClassNotFoundException e) {
//            log.error(" ClassNotFoundException 异常: " + e.getMessage());
//        } catch (Exception e) {
//            log.error(" Exception ! Error Message is: " + e.getMessage());
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                    inputStream.close();
//                } catch (IOException e) {
//                    log.error(" Close BufferedReader InputStream failure");
//                }
//            }
//        }
//        log.warn(" 处理入库的数据 共计 " + record + " 条");
//        return tempRecord == record;
//    }
//
//    /**
//     * -----------------------Insert------------------------------------
//     **/
//    /**
//     * 根据分片键与分片数量取模，指定分属不通Collection，再各自独立入库
//     *
//     * @param list
//     * @return void
//     * @throws
//     * @author WCNGS@QQ.COM
//     * @See
//     * @date 2019/8/1 19:54
//     * @since
//     */
//    protected void paginationDb(List<CsmsNpdata> list) {
//
//        List listPartition1 = new ArrayList(400);
//        List listPartition2 = new ArrayList(400);
//        List listPartition3 = new ArrayList(400);
//        List listPartition4 = new ArrayList(400);
//        List listPartition5 = new ArrayList(400);
//        List listPartition6 = new ArrayList(400);
//        List listPartition7 = new ArrayList(400);
//        List listPartition8 = new ArrayList(400);
//
//        for (CsmsNpdata bean : list) {
//            int condition = FileDocTaskUtil.routerRule(bean.getId(), m2);
//            switch (condition) {
//                case (0):
//                    listPartition1.add(bean);
//                    break;
//                case (1):
//                    listPartition2.add(bean);
//                    break;
//                case (2):
//                    listPartition3.add(bean);
//                    break;
//                case (3):
//                    listPartition4.add(bean);
//                    break;
//                case (4):
//                    listPartition5.add(bean);
//                    break;
//                case (5):
//                    listPartition6.add(bean);
//                    break;
//                case (6):
//                    listPartition7.add(bean);
//                    break;
//                case (7):
//                    listPartition8.add(bean);
//                    break;
//                default:
//                    throw new TaskRuntimeException(InsertConst.NO_PART_ERR_MSG + condition);
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
//        list.clear();
//    }
//
//    /**
//     * 判断List集合是否为空，不为空插入数据
//     *
//     * @param list
//     * @return void
//     * @throws
//     * @author WCNGS@QQ.COM
//     * @See
//     * @date 2019/8/1 18:49
//     * @since
//     */
//    protected void checkEmpty4Commit(List<CsmsNpdata> list) {
//        if (!list.isEmpty()) {
//            submitInsert(list);
//        }
//    }
//
//    /**
//     * 判断List大小是否到达规定阈值，不为空插入数据
//     *
//     * @param list
//     * @return void
//     * @throws
//     * @author WCNGS@QQ.COM
//     * @See
//     * @date 2019/8/1 17:24
//     * @since
//     */
//    protected void checkSize4Commit(List<CsmsNpdata> list) {
//        if (list.size() == PARTION_SIZE) {
//            submitInsert(list);
//        }
//    }
//
//    protected void submitInsert(List<CsmsNpdata> list) {
//        csmsNpdataService.insertBatchByOn(list);
//        list.clear();
//    }
//
//    /**
//     * -----------------------Update------------------------------------
//     **/
//    protected void updateDB(List<CsmsNpdata> list) {
//
//        List listPartition1 = new ArrayList(400);
//        List listPartition2 = new ArrayList(400);
//        List listPartition3 = new ArrayList(400);
//        List listPartition4 = new ArrayList(400);
//        List listPartition5 = new ArrayList(400);
//        List listPartition6 = new ArrayList(400);
//        List listPartition7 = new ArrayList(400);
//        List listPartition8 = new ArrayList(400);
//
//        for (CsmsNpdata bean : list) {
//            int condition = FileDocTaskUtil.routerRule(bean.getId(), m2);
//            switch (condition) {
//                case (0):
//                    listPartition1.add(bean);
//                    break;
//                case (1):
//                    listPartition2.add(bean);
//                    break;
//                case (2):
//                    listPartition3.add(bean);
//                    break;
//                case (3):
//                    listPartition4.add(bean);
//                    break;
//                case (4):
//                    listPartition5.add(bean);
//                    break;
//                case (5):
//                    listPartition6.add(bean);
//                    break;
//                case (6):
//                    listPartition7.add(bean);
//                    break;
//                case (7):
//                    listPartition8.add(bean);
//                    break;
//                default:
//                    throw new TaskRuntimeException(InsertConst.NO_PART_ERR_MSG + condition);
//            }
//
//            checkSize4Update(listPartition1);
//            checkSize4Update(listPartition2);
//            checkSize4Update(listPartition3);
//            checkSize4Update(listPartition4);
//            checkSize4Update(listPartition5);
//            checkSize4Update(listPartition6);
//            checkSize4Update(listPartition7);
//            checkSize4Update(listPartition8);
//        }
//
//        checkEmpty4Update(listPartition1);
//        checkEmpty4Update(listPartition2);
//        checkEmpty4Update(listPartition3);
//        checkEmpty4Update(listPartition4);
//        checkEmpty4Update(listPartition5);
//        checkEmpty4Update(listPartition6);
//        checkEmpty4Update(listPartition7);
//        checkEmpty4Update(listPartition8);
//        list.clear();
//    }
//
//    protected void checkEmpty4Update(List<CsmsNpdata> list) {
//        if (!list.isEmpty()) {
//            submitUpdate(list);
//        }
//    }
//
//    protected void checkSize4Update(List<CsmsNpdata> list) {
//        if (list.size() == PARTION_SIZE) {
//            submitUpdate(list);
//        }
//    }
//
//    protected void submitUpdate(List<CsmsNpdata> list) {
//        csmsNpdataService.updateBatchByOn(list);
//        list.clear();
//    }
//
//    /**
//     * -----------------------Delete------------------------------------
//     **/
//    protected void deleteDB(List<CsmsNpdata> list) {
//
//        List listPartition1 = new ArrayList(400);
//        List listPartition2 = new ArrayList(400);
//        List listPartition3 = new ArrayList(400);
//        List listPartition4 = new ArrayList(400);
//        List listPartition5 = new ArrayList(400);
//        List listPartition6 = new ArrayList(400);
//        List listPartition7 = new ArrayList(400);
//        List listPartition8 = new ArrayList(400);
//
//        for (CsmsNpdata bean : list) {
//            int condition = FileDocTaskUtil.routerRule(bean.getId(), m2);
//            switch (condition) {
//                case (0):
//                    listPartition1.add(bean);
//                    break;
//                case (1):
//                    listPartition2.add(bean);
//                    break;
//                case (2):
//                    listPartition3.add(bean);
//                    break;
//                case (3):
//                    listPartition4.add(bean);
//                    break;
//                case (4):
//                    listPartition5.add(bean);
//                    break;
//                case (5):
//                    listPartition6.add(bean);
//                    break;
//                case (6):
//                    listPartition7.add(bean);
//                    break;
//                case (7):
//                    listPartition8.add(bean);
//                    break;
//                default:
//                    throw new TaskRuntimeException(InsertConst.NO_PART_ERR_MSG + condition);
//            }
//
//            checkSize4Delete(listPartition1);
//            checkSize4Delete(listPartition2);
//            checkSize4Delete(listPartition3);
//            checkSize4Delete(listPartition4);
//            checkSize4Delete(listPartition5);
//            checkSize4Delete(listPartition6);
//            checkSize4Delete(listPartition7);
//            checkSize4Delete(listPartition8);
//        }
//
//        checkEmpty4Delete(listPartition1);
//        checkEmpty4Delete(listPartition2);
//        checkEmpty4Delete(listPartition3);
//        checkEmpty4Delete(listPartition4);
//        checkEmpty4Delete(listPartition5);
//        checkEmpty4Delete(listPartition6);
//        checkEmpty4Delete(listPartition7);
//        checkEmpty4Delete(listPartition8);
//        list.clear();
//    }
//
//    protected void checkEmpty4Delete(List<CsmsNpdata> list) {
//        if (!list.isEmpty()) {
//            submitDelete(list);
//        }
//    }
//
//    protected void checkSize4Delete(List<CsmsNpdata> list) {
//        if (list.size() == PARTION_SIZE) {
//            submitDelete(list);
//        }
//    }
//
//    protected void submitDelete(List<CsmsNpdata> list) {
//        csmsNpdataService.deleteBatchByOn(list);
//        list.clear();
//    }
//
//    /**
//     * @param localDirectoryFile
//     * @return void
//     * @throws
//     * @author WCNGS@QQ.COM
//     * @See
//     * @date 2019/8/1 17:30
//     * @since
//     */
//    public void getFileList(File localDirectoryFile) throws IOException {
//        if (localDirectoryFile.isDirectory()) {
//            File[] files = localDirectoryFile.listFiles();
//            for (File file : files) {
//                getFileList(file);
//            }
//        } else {
//            checkFileList(localDirectoryFile);
//        }
//    }
//
//    /**
//     * 检查对文件列表处理结果
//     *
//     * @param file
//     * @return void
//     * @throws
//     * @author WCNGS@QQ.COM
//     * @See
//     * @date 2019/8/2 10:23
//     * @since
//     */
//    public void checkFileList(File file) {
//        boolean flag = insertDb(file, CsmsNpdata.class.getName());
//        if (!flag) {
//            throw new TaskRuntimeException("Parsing an inbound exception " + file.getName());
//        }
//        NpFileUtil.del(file);
//    }
//}
