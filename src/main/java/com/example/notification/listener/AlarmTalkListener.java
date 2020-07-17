package com.example.notification.listener;

import com.example.notification.client.EmailClient;
import com.example.notification.client.SMSClient;
import com.example.notification.exception.ShouldRetryException;
import com.example.notification.request.SendEmailRequest;
import com.example.notification.request.SendSMSRequest;
import com.example.notification.response.SendEmailResponse;
import com.example.notification.response.SendSMSResponse;
import com.example.notification.template.AlarmTalk;
import com.example.notification.template.EmailSend;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@PropertySource("classpath:api-keys.properties")
public class AlarmTalkListener {
    private final SMSClient smsClient;
    private final EmailClient emailClient;

    @Value("${toast.sms.app_key}")
    private String appKey;

    @Autowired
    public AlarmTalkListener(SMSClient smsClient, EmailClient emailClient) {
        this.smsClient = smsClient;
        this.emailClient = emailClient;
    }

    @KafkaListener(topics = "alarmtalk.notification",
            containerFactory = "alarmTalkKafkaListenerContainerFactory",
            groupId = "alarmtalk-group-id"
    )
    public void handle(AlarmTalk alarmTalk) {
        log.info("alarmTalkListener : alarmtalk.notification event received. : " + alarmTalk.getPhoneNumber() + " : " + alarmTalk.getMessage());
        SendSMSRequest request = SendSMSRequest.builder()
                .body(alarmTalk.getMessage())
                .sendNo(alarmTalk.getPhoneNumber())
                .build();
        request.addRecipientInfo(alarmTalk.getPhoneNumber(), alarmTalk.getCountryCode());

        SendSMSResponse response = smsClient.sendSMS(appKey, request);
        log.info(response.toString());
        response.checkRetryByResponseCode(); // check error handling.

        // FIXME : testinf for retry...
        // throw new ShouldRetryException("retry me!!");

    }

    // DLQ topic handler
    @KafkaListener(topics = "alarmtalk.notification.failures",
            containerFactory = "alarmTalkKafkaListenerContainerFactory",
            groupId = "alarmtalk-fail-group")
    public void handle(ConsumerRecord<Object, AlarmTalk> record) {
        log.info("alarmTalkListener : alarmtalk.notification.failures event received.");
        log.info("headers : " + record.headers().toString());
        log.info("timestamp : " + record.timestamp());
        log.info("payload : " + record.value());
    }


    @KafkaListener(topics = "email.notification",
            containerFactory = "alarmTalkKafkaListenerContainerFactory",
            groupId = "email-group-id")
    public void handle(EmailSend emailSend) {
        log.info("alarmTalkListener : emailSend.notification event received. : ");
        SendEmailRequest request = SendEmailRequest.builder().build();
        SendEmailResponse response = emailClient.sendEmail(appKey, request);
        log.info(response.toString());
    }

}
