package xyz.wongs.weathertop;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xyz.wongs.BitronixApplication;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
@SpringBootTest(classes ={BitronixApplication.class})
public abstract class BaseTest {
}
