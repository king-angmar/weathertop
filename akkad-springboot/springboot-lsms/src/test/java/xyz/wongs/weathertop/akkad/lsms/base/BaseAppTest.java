package xyz.wongs.weathertop.akkad.lsms.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.wongs.weathertop.TaskApplication;

@Component
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TaskApplication.class)
public abstract class BaseAppTest {

}
