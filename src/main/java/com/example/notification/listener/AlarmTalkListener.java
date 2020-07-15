package com.example.notification.listener;

import com.example.notification.client.SMSClient;
import com.example.notification.choices.CountryCode;
import com.example.notification.request.SendSMSRequest;
import com.example.notification.response.SendSMSResponse;
import com.example.notification.template.AlarmTalk;
import lombok.extern.slf4j.Slf4j;
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
    @Value("${toast.sms.app_key}")
    private String appKey;

    @Value("${toast.sms.sendNo}")
    private String sendNo;

    @Autowired
    public AlarmTalkListener(SMSClient smsClient) {
        this.smsClient = smsClient;
    }

    @KafkaListener(topics = "alarmtalk.notification",
            containerFactory = "alarmTalkKafkaListenerContainerFactory",
            groupId = "alarmtalk-group-id"
    )
    public void handle(AlarmTalk alarmTalk) {
        log.info("alarmTalkListener : alarmtalk.notification event received. : " + alarmTalk.getMediaId() + " : " + alarmTalk.getPhoneNumber());
        SendSMSRequest request = SendSMSRequest.builder()
                .body("문자 전송 test : " + alarmTalk.getMediaId())
                .sendNo(sendNo)
                .build();
        request.addRecipientInfo(alarmTalk.getPhoneNumber(), CountryCode.SOUTH_KOREA);

        SendSMSResponse response = smsClient.sendSMS(appKey, request);
        System.out.println(response);

    }
}
