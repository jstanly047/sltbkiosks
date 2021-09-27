package com.slbt.kiosks.sltbkiosksapp;


import com.sltb.kioskslib.library.auth.JWTUtils;
import com.sltb.kioskslib.library.auth.models.AuthenticationRequest;
import com.sltb.kioskslib.library.auth.models.AuthenticationResponse;
import com.sltb.kioskslib.library.auth.models.Status;
import com.sltb.kioskslib.library.model.ActionResponse;
import com.sltb.kioskslib.library.model.kafka.CheckIn;
import com.sltb.kioskslib.library.model.kafka.CheckOut;
import com.sltb.kioskslib.library.model.kafka.TopUp;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class UserAction {
    @Value("${topup.topic.name}")
    String topRequestTopic;
    @Value("${topupreply.topic.name}")
    String topupReplyTopic;
    @Value("${checkin.topic.name}")
    String checkinTopic;
    @Value("${checkinreply.topic.name}")
    String checkinReplyTopic;
    @Value("${checkout.topic.name}")
    String checkoutTopic;
    @Value("${checkoutreply.topic.name}")
    String checkoutReplyTopic;

    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    ReplyingKafkaTemplate<String, TopUp, TopUp> kafkaTopUpTemplate;
    @Autowired
    ReplyingKafkaTemplate<String, CheckIn, CheckIn> kafkaCheckInTemplate;
    @Autowired
    ReplyingKafkaTemplate<String, CheckOut, CheckOut> kafkaCheckOutTemplate;

    @RequestMapping(value="/hellouser")
    public SseEmitter hello(@RequestHeader(name="Authorization") String authHeader)
    {
        String jwt = authHeader.split(" ")[1].trim();
        String userName = jwtUtils.extractUserName(jwt);
        SseEmitter sseEmitter = new SseEmitter();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()->{
            try {
                Thread.sleep(1000);
                sseEmitter.send(new ActionResponse(Status.SUCCESS, "Hello " + userName));
                sseEmitter.complete();
            }catch (IOException e){

            }catch (InterruptedException e) {

            }
        });

        executorService.shutdown();
        return sseEmitter;
        //return ResponseEntity.ok(new ActionResponse(Status.SUCCESS,"Hello " + userName));
    }

    @RequestMapping(value="/topup", method = RequestMethod.POST)
    public TopUp topUp(@RequestBody TopUp topUp) throws InterruptedException, ExecutionException {
        ProducerRecord<String, TopUp> record = new ProducerRecord<String, TopUp>(topRequestTopic, topUp);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, topupReplyTopic.getBytes()));
        RequestReplyFuture<String, TopUp, TopUp> sendAndReceive = kafkaTopUpTemplate.sendAndReceive(record);
        SendResult<String, TopUp> sendResult = sendAndReceive.getSendFuture().get();
        ConsumerRecord<String, TopUp> consumerRecord = sendAndReceive.get();
        return consumerRecord.value();
    }

    @RequestMapping(value="/checkin", method = RequestMethod.POST)
    public CheckIn checkIn(@RequestBody CheckIn checkIn) throws InterruptedException, ExecutionException {
        checkIn.setDate(new Date(System.currentTimeMillis()));
        ProducerRecord<String, CheckIn> record = new ProducerRecord<String, CheckIn>(checkinTopic, checkIn);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, checkinReplyTopic.getBytes()));
        RequestReplyFuture<String, CheckIn, CheckIn> sendAndReceive = kafkaCheckInTemplate.sendAndReceive(record);
        SendResult<String, CheckIn> sendResult = sendAndReceive.getSendFuture().get();
        ConsumerRecord<String, CheckIn> consumerRecord = sendAndReceive.get();
        return consumerRecord.value();
    }

    @RequestMapping(value="/checkout", method = RequestMethod.POST)
    public CheckOut checkOut(@RequestBody CheckOut checkOut) throws InterruptedException, ExecutionException {
        checkOut.setDate(new Date(System.currentTimeMillis()));
        ProducerRecord<String, CheckOut> record = new ProducerRecord<String, CheckOut>(checkoutTopic, checkOut);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, checkoutReplyTopic.getBytes()));
        RequestReplyFuture<String, CheckOut, CheckOut> sendAndReceive = kafkaCheckOutTemplate.sendAndReceive(record);
        SendResult<String, CheckOut> sendResult = sendAndReceive.getSendFuture().get();
        ConsumerRecord<String, CheckOut> consumerRecord = sendAndReceive.get();
        return consumerRecord.value();
    }
}
