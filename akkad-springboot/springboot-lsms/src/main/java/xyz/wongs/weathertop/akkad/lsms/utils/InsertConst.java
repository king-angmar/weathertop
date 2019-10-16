package xyz.wongs.weathertop.akkad.lsms.utils;

/**
 * @Author linwei
 * @Date 2019/8/13
 **/
public class InsertConst {

    private InsertConst(){}

    /**
     *  文件入库成功及入库失败状态
     **/
    public static final String IMPORT_SUC = "70I";
    public static final String IMPORT_EXP = "70IE";
    /**
     *  根据ftpCode获取remotePath和localPath
     **/
    public static final String FTP_CODE = "BIZ_D";
    /**
     *  根据type获取direction
     **/
    public static final String TYPE = "INSERT";
    /**
     *  sftp默认端口号
     **/
    public static final int DEFAULT_PORT = 22;
    /**
     *  压缩文件后缀
     **/
    public static final String GZ_TYPE = ".npd.tar.gz";
    /**
     *  根据groupname获取定时任务对象
     **/
    public static final String GROUP_NAME = "NP_SFTP_INSERT";

    public static final String NO_PART_ERR_MSG =" 没有对应的分片键 MOD Values is ";
}
