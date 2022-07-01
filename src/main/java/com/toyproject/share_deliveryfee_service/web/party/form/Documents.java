package com.toyproject.share_deliveryfee_service.web.party.form;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class Documents {
    private HashMap<String, Object> address;
    private String address_type;
    private Double x;
    private Double y;
    private String address_name;
    private HashMap<String, Object> road_address;
}
