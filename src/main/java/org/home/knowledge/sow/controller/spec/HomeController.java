package org.home.knowledge.sow.controller.spec;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/")
@Slf4j
public class HomeController {

    @GetMapping
    public String hello() {
        log.info("Welcome home");
        return "server-index.html";
    }

}
