package xyz.wongs.weathertop.akkad.lsms.utils;

/**
 * @Author linwei
 * @Date 2019/9/25
 * 比对差异结果类型的枚举类
 **/
public enum DiffTypeConst {

    TYPE_A("A", "NOT IN CSMS_NPDATA"),  //csms_npdata不存在该数据
    TYPE_B("B", "NOT IN LNPDB"),        //lnpdb不存在该数据
    TYPE_C("C", "in_netid differs"),    //in_netid不同
    TYPE_D("D", "out_netid differs"),   //out_netid不同
    TYPE_E("E", "home_netid differs"),  //home_netid不同
    TYPE_F("F", "timestamp differs");   //timestamp不同

    public String type;
    public String remark;

    DiffTypeConst(String type, String remark){
        this.type = type;
        this.remark = remark;
    }
}
