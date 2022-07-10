package com.toyproject.share_deliveryfee_service.web.payment.form;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class ImportTokenRes {
    private int code;
    private String message;
    private List<AccessTokenData> response;
}
