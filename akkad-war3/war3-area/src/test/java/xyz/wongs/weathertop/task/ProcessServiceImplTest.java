package xyz.wongs.weathertop.task;

import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import xyz.wongs.weathertop.base.BaseTest;
import xyz.wongs.weathertop.domain.location.entity.Location;
import xyz.wongs.weathertop.domain.location.service.LocationService;
import xyz.wongs.weathertop.war3.web.utils.ZoneCodeStringUtils;
import xyz.wongs.weathertop.war3.web.zonecode.task.ProcessService;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class ProcessServiceImplTest extends BaseTest {

    private static final String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2019/";
    private final static Logger logger = LoggerFactory.getLogger(ProcessServiceImplTest.class);

    @Autowired
    @Qualifier("processService")
    private ProcessService processService;

    @Autowired
    private LocationService locationService;


    /** 城市
     * @Description
    * @param null
     * @return
     * @throws
     * @date 2020/4/30 0:41
    */
    @Test
    public void initRoot(){
        processService.intiRootUrl(url);
    }


    @Test
    public void intLevelOne() throws Exception {
        exet0(1);
    }


    public void exet0(int pageNum){
        PageInfo<Location> pageInfo = locationService.getLocationsByLv(0,pageNum,30);
        if(pageInfo.getPages()==0 || pageInfo.getPageNum()>pageInfo.getPages()){
            return;
        }
        List<Location> locations = pageInfo.getList();
        Iterator<Location> iter = locations.iterator();
        while(iter.hasNext()){
            Location location = iter.next();
            String uls =  url+location.getUrl();
            processService.getHTML(uls,location);
            location.setFlag("Y");
            locationService.updateByPrimaryKey(location);
        }
        exet0(pageNum+1);
    }


    /**
     *  根据市，初始化 区县
     * @throws Exception
     */
    @Test
    public void initDistrict() throws Exception {
        exet(1);
    }

    public void exet(int pageNum){
        PageInfo<Location> pageInfo = locationService.getLocationsByLv(1,pageNum,30);
        if(pageInfo.getPages()==0 || pageInfo.getPageNum()>pageInfo.getPages()){
            return;
        }
        List<Location> locations = pageInfo.getList();
        Iterator<Location> iter = locations.iterator();
        while(iter.hasNext()){
            Location location = iter.next();
            String url2 = new StringBuilder().append(url).append(location.getUrl()).toString();
            processService.getHTML2(url2,location);
            location.setFlag("Y");
            locationService.updateByPrimaryKey(location);
        }
        exet(pageNum+1);
    }

    /** 乡镇 街道
     * @Description
    * @param null
     * @return 
     * @throws 
     * @date 2020/4/30 0:27
    */
    @Test
    public void intiRuralCommunity(){

        exet3(1);
    }

    public void exet3(int pageNum){
        PageInfo<Location> pageInfo = locationService.getLocationsByLv(2,pageNum,100);
        if(pageInfo.getPages()==0 || pageInfo.getPageNum()>pageInfo.getPages()){
            return;
        }
        List<Location> locations = pageInfo.getList();
        Iterator<Location> iter = locations.iterator();
        Location location = null;
        while(iter.hasNext()){
            location = iter.next();
            String url2 = new StringBuilder().append(url).append(ZoneCodeStringUtils.getUrlStrByLocationCode(location.getLocalCode(), 2)).append(location.getUrl()).toString();
            processService.thridLevelResolve(url2, location, "D");
            try {
                int times = new Random().nextInt(2000);
                Thread.sleep(times);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        exet3(pageNum+1);
    }



    @Test
    public void intiVillage(){
        exet4(1);
    }

    public void exet4(int pageNum){
        PageInfo<Location> pageInfo = locationService.getLocationsByLv(3,pageNum,100);
        if(pageInfo.getPages()==0 || pageInfo.getPageNum()>pageInfo.getPages()){
            return;
        }
        List<Location> locations = pageInfo.getList();
        Iterator<Location> iter = locations.iterator();
        Location location = null;

        while(iter.hasNext()){
            location = iter.next();
            String url2 = new StringBuilder().append(url).append(ZoneCodeStringUtils.getUrlStrByLocationCode(location.getLocalCode(), 2)).append(location.getUrl()).toString();
            processService.thridLevelResolve(url2, location, "T");
            try {
                int times = new Random().nextInt(2000);
                Thread.sleep(times);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        exet4(pageNum+1);
    }

}