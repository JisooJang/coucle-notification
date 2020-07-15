package com.example.notification.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendSMSRequest {
    /*
    {
   "templateId":"TemplateId",
   "body":"본문",
   "sendNo":"15446859",
   "requestDate":"2018-08-10 10:00",
   "senderGroupingKey":"SenderGroupingKey",
   "recipientList":[
      {
         "recipientNo":"01000000000",
         "countryCode":"82",
         "internationalRecipientNo":"821000000000",
         "templateParameter":{
            "key":"value"
         },
         "recipientGroupingKey":"recipientGroupingKey"
      }
   ],
   "userId":"UserId",
   "statsId":"statsId"
}
     */

    private String templateId; // 발송 템플릿 아이디
    private String body; // 메시지 내용. 필수
    private String sendNo; // 발신번호. 필수
    private String requestDate; // 예약일시
    private String senderGroupingKey; // 발신자 그룹 키
    private List<RecipientInfo> recipientList = new ArrayList<>(); // 수신번호 정보. 필수

    public void addRecipientInfo(String recipientNo, String countryCode) {
        this.recipientList.add(new RecipientInfo(recipientNo, countryCode));
    }

    @Data
    @AllArgsConstructor
    private static class RecipientInfo {
        private String recipientNo; // 수신번호. 필수
        private String countryCode; // 국가번호

    }

}
