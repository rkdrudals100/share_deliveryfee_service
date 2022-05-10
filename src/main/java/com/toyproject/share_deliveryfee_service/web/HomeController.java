package com.toyproject.share_deliveryfee_service.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notYet")
@Slf4j
public class HomeController {

    @GetMapping("/notYet")
    public String home(){
        return "index";
    }
}
