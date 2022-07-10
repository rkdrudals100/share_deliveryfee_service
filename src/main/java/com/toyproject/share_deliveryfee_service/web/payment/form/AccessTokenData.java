package com.toyproject.share_deliveryfee_service.web.payment.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenData {
    private String access_token;
    private int now;
    private int expired_at;
}
