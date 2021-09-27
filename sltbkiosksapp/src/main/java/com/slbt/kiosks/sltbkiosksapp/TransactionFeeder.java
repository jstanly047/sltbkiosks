package com.slbt.kiosks.sltbkiosksapp;

import com.sltb.kioskslib.library.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


@Component
public class TransactionFeeder {
    @Value("${txn.topic.name}")
    private String transactionTopic;
    @Autowired
    private KafkaTemplate<String, Transaction> topUpTemplate;

    public void sentTransaction(Transaction transaction){
        ListenableFuture<SendResult<String, Transaction>> future = topUpTemplate.send(transactionTopic, transaction);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Transaction>>() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Failed ========");
            }

            @Override
            public void onSuccess(SendResult<String, Transaction> stringTopUpSendResult) {
                System.out.println("Passed ========");
            }
        });
    }
}