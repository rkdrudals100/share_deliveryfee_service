package com.toyproject.share_deliveryfee_service.web.address;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    private Long id;
    private String streetNameAddress;
    private String oldTypeAddress;

    public AddressDto(String streetNameAddress, String oldTypeAddress){
        this.streetNameAddress = streetNameAddress;
        this.oldTypeAddress = oldTypeAddress;
    }
}
