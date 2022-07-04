package com.toyproject.share_deliveryfee_service.web.party.form;

import com.toyproject.share_deliveryfee_service.web.domain.*;
import com.toyproject.share_deliveryfee_service.web.domain.enums.DeliveryPlatform;
import com.toyproject.share_deliveryfee_service.web.domain.enums.PartyStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartySearchDto {

    private Long id;

    private String title;

    private String introduction;

    private String pickUpLocation;

    private String pickUpLocationDetail;

    private Double latitude;

    private Double longitude;

    private double distanceFromMemberBaseLocation;

    private String restaurant;

    private int totalPrice;

    private int membersNum;

    private int maxMemberNum;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime limitTime;

    @Enumerated(EnumType.STRING)
    private DeliveryPlatform deliveryPlatform;

    @Enumerated(EnumType.STRING)
    private PartyStatus partyStatus;

    private Member organizer;





    //필드 변경 메소드

    public void calcDistanceFromMemberBaseLocation(double distance){

        this.distanceFromMemberBaseLocation = distance;
    }
}
