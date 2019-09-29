package xyz.wongs.weathertop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.FutureResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

@Slf4j
@Service
public class UserService {

    @Async
    public String findUser(Long id){
        log.error(" UserService findUser is "+ Thread.currentThread().getName());
        return "Wongs";
    }

    @Async
    public Future<String> findUserById(Long id){
        log.error(" UserService findUserById is "+ Thread.currentThread().getName());
        return new AsyncResult<String>("Wongs");
    }
}
