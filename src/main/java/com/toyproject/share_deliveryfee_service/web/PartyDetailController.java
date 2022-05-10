package com.toyproject.share_deliveryfee_service.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class PartyDetailController {

    @GetMapping("/partyDetails")
    public String detail(){

        return "partyDetails";
    }
}
