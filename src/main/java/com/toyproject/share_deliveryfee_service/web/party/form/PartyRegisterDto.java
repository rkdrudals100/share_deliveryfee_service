package com.toyproject.share_deliveryfee_service.web.party.form;

import com.toyproject.share_deliveryfee_service.web.domain.DeliveryPlatform;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PartyRegisterDto {

    private String title;

    private String introduction;

    private String pickUpLocation;

    private String pickUpLocationDetail;

    private String restaurant;

    private int totalPrice;

    private int membersNum;

    private String limitTime;

    // int로 받아서 컨트롤러에서 enum으로 치환
    private int deliveryPlatform;
}
