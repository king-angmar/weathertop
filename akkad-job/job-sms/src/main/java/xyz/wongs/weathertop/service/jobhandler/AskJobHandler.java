package xyz.wongs.weathertop.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.wongs.weathertop.base.config.Springboot2Utils;

import java.util.concurrent.TimeUnit;



@Slf4j
@JobHandler(value="askJobHandler")
@Component
public class AskJobHandler extends IJobHandler {

	@Autowired
    Springboot2Utils springboot2Utils;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		log.error("开始执行任务 XXL-JOB");
		log.error(" 当前端口为 "+ springboot2Utils.getPort());
		XxlJobLogger.log("开始执行任务 XXL-JOB");
		for (int i = 0; i < 5; i++) {
			XxlJobLogger.log(" beat at:" + i+" 端口: "+springboot2Utils.getPort());
			TimeUnit.SECONDS.sleep(2);
		}
		return SUCCESS;
	}

}
