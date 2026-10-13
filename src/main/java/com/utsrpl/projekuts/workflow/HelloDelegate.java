package com.utsrpl.projekuts.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class HelloDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        String message = "Hello World dari proses Camunda BPMN";

        execution.setVariable("message", message);

        System.out.println(message);
    }
}