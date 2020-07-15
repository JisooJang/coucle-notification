package com.example.notification.client;

import com.example.notification.response.SendSMSResponse;

import java.util.Map;

public class SMSClientFallback implements SMSClient {
    @Override
    public SendSMSResponse sendSMS(String appKey, Map<String, String> request) {
        return null;
    }
}
