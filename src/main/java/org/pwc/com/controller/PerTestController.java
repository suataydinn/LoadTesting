package org.pwc.com.controller;

import org.pwc.com.model.TestConfig;
import org.pwc.com.service.JMeterTestPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/performance")
public class PerTestController {

    @Autowired
    private JMeterTestPlanService jMeterTestPlanService;

    @PostMapping("/test")
    public ResponseEntity<?> runTest(@RequestBody TestConfig testConfig) {
        try {
            if (testConfig.getDomain() == null || testConfig.getDomain().isEmpty()) {
                return ResponseEntity.badRequest().body("Domain cannot be null or empty");
            }

            jMeterTestPlanService.createAndRunTestPlan(testConfig);
            return ResponseEntity.ok("Test execution started successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error during test execution: " + e.getMessage());
        }
    }
}