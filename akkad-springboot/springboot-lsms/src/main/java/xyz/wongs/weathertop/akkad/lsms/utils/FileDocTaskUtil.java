package xyz.wongs.weathertop.akkad.lsms.utils;

import xyz.wongs.weathertop.akkad.lsms.analydb.entity.SmNpdb;
import xyz.wongs.weathertop.akkad.lsms.common.TaskRuntimeException;
import xyz.wongs.weathertop.base.utils.FileDocUtil;

import java.math.BigInteger;

public class FileDocTaskUtil extends FileDocUtil {


    public static SmNpdb getBeanInstance(String clazzName) throws ClassNotFoundException,InstantiationException, IllegalAccessException{
        Class<?> clazz = Class.forName(clazzName);
        return (SmNpdb)clazz.newInstance();
    }

    /** 根据分片键，与数据库分片数 之间取模，用于判断需要插入的分片
     * @author WCNGS@QQ.COM
     * @See
     * @date 2019/8/1 17:25
     * @param id 标识
     * @param md 模的数量
     * @return int
     * @throws
     * @since
     */
    public static int routerRule(String id,BigInteger md){
        int res = 0;
        try {
            BigInteger ans = new BigInteger(id);
            res = ans.mod(md).intValue();
        } catch (Exception e) {
            throw new TaskRuntimeException(" 获取分片键异常 "+ id);
        }
        return res;
    }

}
