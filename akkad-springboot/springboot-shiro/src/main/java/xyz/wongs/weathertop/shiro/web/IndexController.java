package xyz.wongs.weathertop.shiro.web;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequestMapping("/")
public class IndexController {

	@RequestMapping("/index") public String index() {
		log.debug("-------------index------------");
		return "index";
	}

	@RequestMapping("/home") public String toHome() {
		log.debug("===111-------------home------------");
		return "home";
	}
	@RequestMapping("/login")
	public String toLogin() {
		log.debug("===111-------------login------------");
		return "login";
	}

	@RequestMapping("/{page}") public String toPage(
			@PathVariable("page") String page) {
		log.debug("-------------toindex------------" + page);
		return page;
	}
}
