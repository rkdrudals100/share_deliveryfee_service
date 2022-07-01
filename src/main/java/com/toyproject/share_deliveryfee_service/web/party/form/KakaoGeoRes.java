package com.toyproject.share_deliveryfee_service.web.party.form;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class KakaoGeoRes {
    private HashMap<String, Object> meta;
    private List<Documents> documents;
}
