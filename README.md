# coucle-notification
- event consumer app handling notification action(emailSender, SmsSender...) using Apache Kafka.
- notification service managed by `spring-cloud-eureka`
- using `spring-cloud-openfeign` for REST client.

## 제공 기능 (Kafka Consumer)
- **문자 메시지 전송**
    - topic name : `alarmtalk.notification`
    - data : 
        - countryCode (String - required) : 메시지 내용에 담기는 유저 아이디. ex) "82"
        - phoneNumber (String - required) : 메시지 수신 번호.
        - message (String - required) : 메시지 전송 내용. 최대 90바이트(한글 45자, 영문 90자)
    - NHN TOAST API 사용 (spring-cloud-feignClient를 통해 rest-client 구현)
    - https://docs.toast.com/ko/Notification/SMS/ko/api-guide/
- **이메일 전송**
    - topic name : `email.notification`
    - data : 
        - senderAddress (String - required) : 발신자 이메일 주소. (최대 100자)
        - senderName (String - optinal) : 발신자 이름 (최대 100자)
        - title (String - required) : 전송할 메일 제목(최대 998자)
        - body (String - required) : 전송할 메일 내용
        - receiveMailAddr (String - required) : 수신자 이메일 주소. 
    - NHN TOAST API 사용 (spring-cloud-feignClient를 통해 rest-client 구현)
    - https://docs.toast.com/ko/Notification/Email/ko/api-guide/


## Kafka Error handling
- rest 통신 응답 결과가 클라이언트 측의 에러 코드인 경우를 제외하고, 기본 5초 간격으로 retry 5회 실행 후, <br>
이후에도 에러가 발생하거나 rest 에러 코드가 지속되는 경우 `dead-letter-topic`으로 전달. (`{기존 레코드 토픽명}.failure`)
- 각 dead-letter-topic도 `KafkaListener`를 통해 consume하고 있으며, 구체적인 레코드 헤더, 시간, 내용 등 로그 처리. 
- NHN TOAST API 에러 코드 참고 : https://docs.toast.com/ko/Notification/SMS/ko/error-code/