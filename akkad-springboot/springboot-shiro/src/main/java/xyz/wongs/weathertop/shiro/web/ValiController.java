package xyz.wongs.weathertop.shiro.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.wongs.weathertop.shiro.vo.AcctVo;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Slf4j
@Validated
@RestController
public class ValiController {

    @GetMapping("/hello")
    public String hello(@NotBlank(message = "{required}") String userName,@NotBlank(message = "{required}") String passWord,
                        @Email(message = "{invalid}") String email) {
        log.error("===========");
        return "Hello World";
    }

    @GetMapping("/hello2")
    public String hello2(@Valid AcctVo acctVo) {
        return "Hello World2";
    }
}
