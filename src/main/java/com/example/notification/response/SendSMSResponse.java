package com.example.notification.response;

import com.example.notification.exception.ShouldRetryException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendSMSResponse {
    /*
    {
   "header":{
      "isSuccessful":true,
      "resultCode":0,
      "resultMessage":"SUCCESS"
   },
   "body":{
      "data":{
         "requestId":"20180810100630ReZQ6KZzAH0",
         "statusCode":"2",
         "senderGroupingKey":"SenderGroupingKey",
         "sendResultList":[
            {
               "recipientNo":"01000000000",
               "resultCode":0,
               "resultMessage":"SUCCESS",
               "recipientSeq":1,
               "recipientGroupingKey":"RecipientGroupingKey"
            }
         ]
      }
   }
}
     */

    private Boolean isSuccessful;
    private Integer resultCode; // 실패 코드. 성공시 0
    private String resultMessage; // 실패 메시지. 성공시 "SUCCESS"

    private String requestId; // 요청 코드
    private String statusCode; // 요청 상태 코드(1:요청 중, 2:요청 완료, 3:요청 실패)

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
        this.statusCode = bodyData.get("statusCode");
    }

    // TODO : Listener에서 체크한후, rest 서버 복구 가능 에러면 error-handler(retry)처리. (기본 10회 재시도 후, 실패 레코드 로그 처리)
    // TODO: 클라이언트 관련 에러면 retry X. dead-letter-topic에 로그 목적으로 실패 메시지 남기고 return. (메시지 처리-완료 처리)
    public void checkRetryByResponseCode() {
        // https://docs.toast.com/ko/Notification/SMS/ko/error-code/
        if(this.resultCode == null) return;
        if(this.resultCode == -9999 || this.resultCode == -2021) {
            throw new ShouldRetryException(this.resultMessage);
        } else {
            throw new IllegalArgumentException(this.resultMessage);
        }
    }
}
