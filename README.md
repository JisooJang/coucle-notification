# coucle-notification
- event consumer app handling notification action(emailSender, SmsSender...) using Apache Kafka.
- notification service managed by `spring-cloud-eureka`
- using `spring-cloud-openfeign` for rest client.

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
    - data : (구현 예정)
    - NHN TOAST API 사용 (spring-cloud-feignClient를 통해 rest-client 구현)
    - https://docs.toast.com/ko/Notification/Email/ko/api-guide/


## Kafka 에러 핸들링 및 Retry 정책
- rest 통신 응답 결과가 클라이언트 측의 에러 코드인 경우를 제외하고, 기본 5초 간격으로 retry 5회 실행 후, <br>
이후에도 에러가 발생하거나 rest 에러 코드가 지속되는 경우 dead-letter-topic으로 전달. (`{기존 레코드 토픽명}.failure`)
- NHN TOAST API 에러 코드 참고 : https://docs.toast.com/ko/Notification/SMS/ko/error-code/