package org.home.knowledge.sow.controller.spec;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/server-home")
@Slf4j
public class HomeController {

    @GetMapping
    public String hello() {
        log.info("Welcome home");
        return "server-index.html";
    }

    @GetMapping("/json")
    public ResponseEntity<String> helloFromJson() {
        log.info("Welcoming from json response");
        return ResponseEntity.ok("Welcome home");
    }

}
