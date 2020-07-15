package com.example.notification.client;

import com.example.notification.request.SendSMSRequest;
import com.example.notification.response.SendSMSResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "smsClient", url = "https://api-sms.cloud.toast.com")
public interface SMSClient {

    @PostMapping("/sms/v2.3/appKeys/{appKey}/sender/sms")
    SendSMSResponse sendSMS(@PathVariable("appKey") String appKey,
                            @RequestBody SendSMSRequest request);
}
