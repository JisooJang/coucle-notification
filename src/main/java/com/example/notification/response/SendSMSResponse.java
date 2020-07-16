package com.example.notification.response;

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
    private void unpackNestedBody(Map<String, Object> body) {
        Map<String,String> bodyData = (Map<String,String>) body.get("data");
        this.requestId = bodyData.get("requestId");
        this.statusCode = bodyData.get("statusCode");
    }
}
