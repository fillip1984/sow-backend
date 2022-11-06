package org.home.knowledge.sow.controller;

import java.time.LocalDateTime;

import org.home.knowledge.sow.model.dto.DataExport;
import org.home.knowledge.sow.service.AdminService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final ObjectMapper objectMapper;

    public AdminController(AdminService adminService, ObjectMapper objectMapper) {
        this.adminService = adminService;
        this.objectMapper = objectMapper;
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

    @PostMapping("/import")
    public ResponseEntity<String> importData(MultipartFile file) {
        try {
            log.info("Performing import");
            var data = objectMapper.readValue(file.getInputStream(), DataExport.class);

            adminService.importData(data);

            return ResponseEntity.ok("success");
        } catch (Exception e) {
            var msg = "Exception occurred while attempting to import data";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportData() {
        try {
            log.info("Performing export");
            DataExport data = adminService.exportData();
            StringBuilder out = new StringBuilder();

            out.append(objectMapper.writeValueAsString(data));

            log.trace("Json export result: {}", out.toString());

            ByteArrayResource resource = new ByteArrayResource(out.toString().getBytes());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=export_" + LocalDateTime.now() + ".json");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            log.info("Performed export");

            // @formatter:off
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
            // @formatter:on
        } catch (Exception e) {

            var msg = "Exception occurred while exporting data";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
