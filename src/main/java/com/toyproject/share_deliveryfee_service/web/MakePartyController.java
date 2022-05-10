package com.toyproject.share_deliveryfee_service.web;

import com.toyproject.share_deliveryfee_service.web.address.AddressDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/makeParty_dummy")
public class MakePartyController {

    @GetMapping("")
    public String newForm(Model model){

        //테스트 주소 데이터 주입
        List<AddressDto> testDatas = new ArrayList();

        AddressDto testData1 = new AddressDto("안양시", "학의로390");
        AddressDto testData2 = new AddressDto("안양시", "학의로391");
        AddressDto testData3 = new AddressDto("안양시", "학의로420");
        AddressDto testData4 = new AddressDto("수원시", "테헤란로");
        AddressDto testData5 = new AddressDto("수원시", "테헤란로2");
        AddressDto testData6 = new AddressDto("서울시", "새해로");
        AddressDto testData7 = new AddressDto("서울시", "한강로");
        AddressDto testData8 = new AddressDto("서울시", "잠실나루로");
        AddressDto testData9 = new AddressDto("서울시", "한강로");
        AddressDto testData10 = new AddressDto("강릉시", "해의로");
        AddressDto testData11 = new AddressDto("안양시", "평의로");
        testDatas.add(testData1);
        testDatas.add(testData2);
        testDatas.add(testData3);
        testDatas.add(testData4);
        testDatas.add(testData5);
        testDatas.add(testData6);
        testDatas.add(testData7);
        testDatas.add(testData8);
        testDatas.add(testData9);
        testDatas.add(testData10);
        testDatas.add(testData11);

        model.addAttribute("testDatas", testDatas);

        return "makeParty";
    }
}
