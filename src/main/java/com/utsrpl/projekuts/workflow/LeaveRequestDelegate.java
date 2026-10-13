package com.utsrpl.projekuts.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class LeaveRequestDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        Object hariVariable = execution.getVariable("hari");
        Integer hari = hariVariable instanceof Number ? ((Number) hariVariable).intValue() : null;
        if (hari == null || hari < 1) {
            throw new IllegalArgumentException("Jumlah hari izin harus lebih besar dari 0");
        }

        execution.setVariable("status", hari <= 3 ? "APPROVED" : "REJECTED");
    }
}
