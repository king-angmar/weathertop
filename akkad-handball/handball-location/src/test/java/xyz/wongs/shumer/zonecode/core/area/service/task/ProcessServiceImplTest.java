package xyz.wongs.shumer.zonecode.core.area.service.task;

import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xyz.wongs.shumer.App;
import xyz.wongs.shumer.handball.location.entity.Location;
import xyz.wongs.shumer.handball.location.service.LocationService;
import xyz.wongs.shumer.handball.task.ProcessService;
import xyz.wongs.shumer.handball.utils.ZoneCodeStringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
@SpringBootTest(classes ={App.class})
public class ProcessServiceImplTest {
    private static final String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/";
    private final static Logger logger = LoggerFactory.getLogger(ProcessServiceImplTest.class);

    @Autowired
    @Qualifier("processService")
    private ProcessService processService;

    @Autowired
    private LocationService locationService;


    @Test
    public void initRoot(){
        processService.intiRootUrl(url);
    }


    @Test
    public void intLevelOne() throws Exception {
        exet0(0);
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


    @Test
    public void initCounty() throws Exception {
        exet(0);
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



    @Test
    public void intiRuralCommunity(){
        exet3(0);
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
        exet4(0);
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