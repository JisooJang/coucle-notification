package com.example.notification.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendEmailResponse {
    private Boolean isSuccessful;
    private Integer resultCode; // 실패 코드. 성공시 0
    private String resultMessage; // 실패 메시지. 성공시 "SUCCESS"

    private String requestId; // 요청 코드
    private Integer receiverResultCode; // 수신자 발송 요청 결과 코드
    private String receiverResultMessage; // 수신자 발송 요청 결과 메시지

    @JsonProperty("header")
    public void unpackNestedHeader(Map<String, Object> header) {
        this.isSuccessful = (Boolean) header.get("isSuccessful");
        this.resultCode = (Integer) header.get("resultCode");
        this.resultMessage = (String) header.get("resultMessage");
    }

    @JsonProperty("body")
    public void unpackNestedBody(Map<String, Object> body) {
        Map<String,String> bodyData = (Map<String,String>) body.get("data");
        this.requestId = bodyData.get("requestId");
        this.receiverResultCode = Integer.parseInt(bodyData.get("resultCode"));
        this.receiverResultMessage = bodyData.get("resultMessage");
    }

}
