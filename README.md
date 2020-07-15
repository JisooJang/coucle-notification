# coucle-notification
- event consumer app handling notification action(emailSender, SmsSender...) using Apache Kafka.
- notification service managed by `spring-cloud-eureka`
- using `spring-cloud-openfeign` for rest client.

## 제공 기능 (Kafka Consumer)
- 문자 메시지 전송
    - topic name : `alarmtalk.notification`
    - data : 
        - mediaId (String - required) : 메시지 내용에 담기는 유저 아이디. 
        - phoneNumber (String - required) : 메시지 수신 번호.
    - NHN TOAST API 사용
    - https://docs.toast.com/ko/Notification/SMS/ko/api-guide/
- 이메일 전송
    - topic name : `email.notification`
    - data :
    - NHN TOAST API 사용
    - https://docs.toast.com/ko/Notification/Email/ko/api-guide/
