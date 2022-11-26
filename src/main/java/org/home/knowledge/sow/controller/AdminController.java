package org.home.knowledge.sow.controller;

import org.home.knowledge.sow.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/load-sample-data")
    public ResponseEntity<String> loadSampleData() {
        try {
            log.info("Loading sample data");
            adminService.loadSampleData();
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            var msg = "Exception occurred while loading sample data";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

}
