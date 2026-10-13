package com.utsrpl.projekuts.controller;

import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class PageController {

    private final RuntimeService runtimeService;

    public PageController(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @GetMapping("/hello-jsp")
    public String helloJsp() {
        return "hello";
    }

    @GetMapping("/pengajuan-izin")
    public String pengajuanIzin() {
        return "pengajuan-izin";
    }

    @PostMapping("/pengajuan-izin")
    public String submitPengajuanIzin(
            @RequestParam String nama,
            @RequestParam String alasan,
            @RequestParam int hari,
            Model model) {
        if (nama.isBlank() || alasan.isBlank() || hari < 1) {
            model.addAttribute("error", "Nama, alasan, dan jumlah hari wajib diisi dengan benar.");
            model.addAttribute("nama", nama);
            model.addAttribute("alasan", alasan);
            model.addAttribute("hari", hari);
            return "pengajuan-izin";
        }

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "pengajuanIzin",
                Map.of("nama", nama, "alasan", alasan, "hari", hari));

        model.addAttribute("nama", nama);
        model.addAttribute("alasan", alasan);
        model.addAttribute("hari", hari);
        model.addAttribute("status", hari <= 3 ? "APPROVED" : "REJECTED");
        model.addAttribute("processInstanceId", processInstance.getId());
        return "pengajuan-izin";
    }
}