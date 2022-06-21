package com.toyproject.share_deliveryfee_service.web.sms;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


//2022-04-20 강경민
//SMS 전송 구현
@Slf4j
@RequiredArgsConstructor
public class SendSMSTwilio {

    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");

    // 전송 함수
    public static int sendSMS(String countryNum, String phoneNum) {
        // ACCOUNT_SID, AUTH_TOKEN 정보
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // 휴대폰 인증번호 생성
        int authNum = randomRange(100000, 999999);

        // 전송 대상 번호
        String sendTargetNum = "+" + countryNum + phoneNum;

        // 전송할 메시지
        String authMsg = "share_deliveryfee_service 인증번호: [" + authNum + "]";

        Message message = Message.creator(
            new PhoneNumber(sendTargetNum),
            new PhoneNumber("+13254405561"),
            authMsg).create();

        return authNum;
    }

    // 인증번호 범위 지정
    public static int randomRange(int n1, int n2) {
        return (int) (Math.random() * (n2 - n1 + 1)) + n1;
    }
}
