package com.example.notification.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
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
    private Header header;
    private Body body;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Header {
        private boolean isSuccessful;
        private int resultCode;
        private String resultMessage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Body {
        private Map<String, ?> properties = new HashMap<>();
    }
}
