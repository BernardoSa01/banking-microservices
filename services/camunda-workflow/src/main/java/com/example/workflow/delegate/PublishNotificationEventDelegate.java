package com.example.workflow.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class PublishNotificationEventDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {

        String transactionId = (String) execution.getVariable("transactionId");
        String accountId = (String) execution.getVariable("accountId");

        System.out.println("Publishing notification event for transaction: " + transactionId);
        System.out.println("AccountId: " + accountId);

        // publicar no kafka
    }
}
