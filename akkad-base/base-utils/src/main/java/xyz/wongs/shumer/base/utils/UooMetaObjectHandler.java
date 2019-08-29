//package cn.ffcs.np.base.utils;
//
//import com.baomidou.mybatisplus.mapping.MetaObjectHandler;
//import org.apache.ibatis.reflection.MetaObject;
//import org.springframework.stereotype.Component;
//
//import java.utils.Date;
//
///** mybatisplus自定义填充公共字段 ,即没有传的字段自动填充
// *  yml 加配置 meta-object-handler: UooMetaObjectHandler
// *  @author wudj
// *  @Date 2018/11/14
// * */
//@Component
//public class UooMetaObjectHandler extends MetaObjectHandler {
//    //新增填充
//    @Override
//    public void insertFill(MetaObject metaObject) {
//
//        if(null == getFieldValByName("statusCd", metaObject)){
//            setFieldValByName("statusCd", "1000", metaObject);
//        }
//        if(null == getFieldValByName("statusDate", metaObject)){
//            setFieldValByName("statusDate", new Date(), metaObject);
//        }
//        if(null == getFieldValByName("createDate", metaObject)){
//            setFieldValByName("createDate", new Date(), metaObject);
//        }
//        if(null == getFieldValByName("createUser", metaObject)){
//            setFieldValByName("createUser", -1L, metaObject);
//        }
//        if(null == getFieldValByName("updateDate", metaObject)){
//            setFieldValByName("updateDate", new Date(), metaObject);
//        }
//        if(null == getFieldValByName("updateUser", metaObject)){
//            setFieldValByName("updateUser", -1L, metaObject);
//        }
//        if(this.hasGetter("enableDate", metaObject) && null == getFieldValByName("enableDate", metaObject)){
//            setFieldValByName("enableDate", new Date(), metaObject);
//        }
//        if(this.hasGetter("disableDate", metaObject) && null == getFieldValByName("disableDate", metaObject)){
//            setFieldValByName("disableDate", DateUtils.getDatebystr("20991231", "yyyyMMdd"), metaObject);
//        }
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//
//        setFieldValByName("updateDate", new Date(), metaObject);
//        setFieldValByName("updateUser", -2L, metaObject);
//
//    }
//
//    public boolean hasGetter(String fieldName, MetaObject metaObject){
//        if (metaObject.hasGetter(fieldName)) {
//            return true;
//        } else if (metaObject.hasGetter(META_OBJ_PREFIX + "." + fieldName)) {
//            return true;
//        }
//        return false;
//    }
//
//}
//
