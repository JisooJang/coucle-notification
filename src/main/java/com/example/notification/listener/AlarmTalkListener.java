package com.example.notification.listener;

import com.example.notification.template.AlarmTalk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AlarmTalkListener {

    @KafkaListener(topics = "alarmtalk.notification",
            containerFactory = "alarmTalkKafkaListenerContainerFactory",
            groupId = "alarmtalk-group-id"
    )
    public void handle(AlarmTalk alarmTalk) {
        log.debug("alarmTalkListener : alarmtalk.notification event received. : " + alarmTalk.getMediaId() + " : " + alarmTalk.getPhoneNumber());
        System.out.println("alarmTalkListener : alarmtalk.notification event received. : " + alarmTalk.getMediaId() + " : " + alarmTalk.getPhoneNumber());
    }
}
