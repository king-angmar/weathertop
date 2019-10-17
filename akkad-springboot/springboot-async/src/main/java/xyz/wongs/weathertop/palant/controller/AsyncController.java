package xyz.wongs.weathertop.palant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.wongs.weathertop.service.UserService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@RestController
public class AsyncController {

    @Autowired
    private UserService userService;

    @GetMapping("/testAsync")
    public String testAsync() throws InterruptedException, ExecutionException {
        Long id = 1L;
        String userName = userService.findUser(id);
        log.error(" AsyncController "+ Thread.currentThread().getName());
        log.error(" userName value is "+ userName);

        Future<String> future = userService.findUserById(id);
        String lastName =future.get();
        log.error(" lastName value is "+ lastName);
        return userName;
    }
}
