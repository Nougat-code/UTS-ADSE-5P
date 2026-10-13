package com.utsrpl.projekuts.controller;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcessController {

    private final RuntimeService runtimeService;

    public ProcessController(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @GetMapping("/start-process")
    public Map<String, Object> startProcess() {
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("helloWorld");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("processInstanceId", processInstance.getId());
        response.put("message", "Proses BPMN berhasil dijalankan");

        return response;
    }
}