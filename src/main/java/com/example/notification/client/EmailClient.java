package com.example.notification.client;

import com.example.notification.request.SendEmailRequest;
import com.example.notification.response.SendEmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "emailClient", url = "https://api-mail.cloud.toast.com")
public interface EmailClient {
    @PostMapping("/email/v1.6/appKeys/{appKey}/sender/mail")
    SendEmailResponse sendEmail(@PathVariable("appKey") String appKey,
                              @RequestBody SendEmailRequest request);
}
