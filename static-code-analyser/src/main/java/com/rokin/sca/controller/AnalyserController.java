package com.rokin.sca.controller;

import com.rokin.sca.service.AnalyserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analyse")
public class AnalyserController {

    @Autowired
    private AnalyserService analyserService;

    @GetMapping("/dependencies")
    public Map<String, List<String>> getDependencyData(@RequestParam("path") String projectPath) throws FileNotFoundException {
        return analyserService.getDependencyData(projectPath);
    }

    @GetMapping("/inheritance")
    public String getInheritanceData(@RequestParam("path") String projectPath) throws FileNotFoundException {
        return analyserService.getInheritanceData(projectPath);
    }

    @GetMapping("/microservices")
    public Map<String, List<String>> getMicroserviceClasses(@RequestParam("path") String projectPath) throws FileNotFoundException {
        return analyserService.getMicroserviceClasses(projectPath);
    }
}
