package com.example.notification.client;

import com.example.notification.request.SendSMSRequest;
import com.example.notification.response.SendSMSResponse;


public class SMSClientFallback implements SMSClient {
    @Override
    public SendSMSResponse sendSMS(String appKey, SendSMSRequest request) {
        return null;
    }
}
