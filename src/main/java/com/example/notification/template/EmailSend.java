package com.example.notification.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailSend {
    private String senderAddress;
    private String senderName;
    private String title;
    private String body;
    private String receiveMailAddr;
    private String receiveType; //  (MRT0 : 받는사람 , MRT1 : 참조, MRT2 : 숨은참조)
}
