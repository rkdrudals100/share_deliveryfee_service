package com.toyproject.share_deliveryfee_service.web.member;

import com.toyproject.share_deliveryfee_service.web.domain.*;
import com.toyproject.share_deliveryfee_service.web.domain.enums.MemberRole;
import com.toyproject.share_deliveryfee_service.web.member.form.BaseLocationChangeDto;
import com.toyproject.share_deliveryfee_service.web.member.form.MemberDupCheckIdDto;
import com.toyproject.share_deliveryfee_service.web.member.form.MemberRegisterDto;
import com.toyproject.share_deliveryfee_service.web.member.validator.BasePickupLocationChangeValidator;
import com.toyproject.share_deliveryfee_service.web.member.validator.MemberDupCheckValidator;
import com.toyproject.share_deliveryfee_service.web.member.validator.MemberRegisterValidator;
import com.toyproject.share_deliveryfee_service.web.notificationLog.NotificationLogRepository;
import com.toyproject.share_deliveryfee_service.web.notificationLog.NotificationLogService;
import com.toyproject.share_deliveryfee_service.web.party.MemberPartyRepository;
import com.toyproject.share_deliveryfee_service.web.party.PartyRepository;
import com.toyproject.share_deliveryfee_service.web.party.PartyService;
import com.toyproject.share_deliveryfee_service.web.sms.SendSMSTwilio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberRegisterValidator memberRegisterValidator;
    private final MemberDupCheckValidator memberDupCheckValidator;
    private final BasePickupLocationChangeValidator basePickupLocationChangeValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MemberRepository memberRepository;
    private final MemberPartyRepository memberPartyRepository;
    private final PartyRepository partyRepository;
    private final NotificationLogRepository notificationLogRepository;

    private final MemberService memberService;
    private final PartyService partyService;
    private final NotificationLogService notificationLogService;






    @GetMapping("admin/hello")
    @ResponseBody
    public String admin(){
        return "<h1>admin</h1>";
    }




    @GetMapping("/login")
    public String login(Model model, Principal principal, @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception){
        if(principal != null) {
            return "redirect:/";
        }

        if (error != null){
            model.addAttribute("error", error);
            model.addAttribute("exception", exception);
        }
        return "login";
    }




    @GetMapping("/signUp")
    public String signUp(Model model) {

        model.addAttribute("member", new MemberRegisterDto());
        return "signUp";
    }







    //2022-04-21 ?????????
    //?????? ?????? ??? ?????? ??????
    @PostMapping("/signUp")
    public String registerMember(@Validated @ModelAttribute("member") MemberRegisterDto memberRegisterDto, BindingResult bindingResult, HttpServletRequest request) {

        HttpSession session = request.getSession();

        memberRegisterValidator.validate(memberRegisterDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            return "signUp";
        }

        String phoneNumPlusAuthNum = memberRegisterDto.getMemberPhoneNum() + "_" + memberRegisterDto.getPhoneCertificationNum();

        if(!bCryptPasswordEncoder.matches(phoneNumPlusAuthNum , String.valueOf(session.getAttribute("authNum")))){
            bindingResult.reject("certificationFail");
            log.info("??????????????????");

            return "signUp";
        }
        session.removeAttribute(phoneNumPlusAuthNum);

        Member saveMember = memberService.registerMember(memberRegisterDto.getMemberId(), bCryptPasswordEncoder.encode(memberRegisterDto.getMemberPassword()),
                memberRegisterDto.getMemberEmail(), memberRegisterDto.getMemberPhoneNum(), MemberRole.USER, "ROLE_USER");

        log.info("'{}'??? member??? ?????? ??????", memberRegisterDto.getMemberId());

        notificationLogService.newNotificationLog(saveMember,
                "????????? ?????????????????????.", "/accountInfo/profile");
        notificationLogService.newNotificationLog(saveMember,
                "?????? ??????????????? ??????????????????. ????????? ??? ?????? ?????? ????????? ???????????? ????????? ???????????? ????????? ???????????????.", "/accountInfo/profile");

        return "redirect:/login"; // ?????? ?????? ?????????
    }






    @PostMapping("/signUp/dupCheckId2")
    public String dupFormTest(@Validated @ModelAttribute("member") MemberDupCheckIdDto memberDupCheckIdDto, BindingResult bindingResult){

//        log.info("???????????? ?????? ?????? ??? ???????????? ??????");
//        log.info("{}", memberDupCheckIdDto.toString());
//        log.info("{}", memberDupCheckIdDto.getMemberId());

        memberDupCheckValidator.validate(memberDupCheckIdDto, bindingResult);

//        log.info("????????? ?????? ??????");
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            return "signUp";
        }

        return "redirect:/signUp";
    }






    //2022-0422 ?????????
    //????????? ?????? ?????? ?????? ??????
    @PostMapping("/signUp/dupCheckId")
    @ResponseBody
    public Map<String, String> dupCheckMemberId(@RequestBody Map<String, Object> inputMap, BindingResult bindingResult){

        String getMemberId = (String) inputMap.get("getMemberId");
        Map<String, String> returnMap = new HashMap<>();

        MemberDupCheckIdDto memberDupCheckIdDto = MemberDupCheckIdDto.builder()
                .memberId(getMemberId)
                .build();

//        memberDupCheckValidator.validate(memberDupCheckIdDto, bindingResult);

        memberService.DupCheckByUserId(getMemberId, returnMap);

        return returnMap;
    }




    //2022-0422 ?????????
    //?????? ?????? ?????? ?????? ?????? ??? ?????? ??????
    @PostMapping("/signUp/sendAuthenticationNum")
    @ResponseBody
    public Map<String, String> sendAuthenticationNum(@RequestBody Map<String, Object> inputMap, HttpSession session){
        log.info("'{}' ??? ???????????? ?????? ??????", inputMap.get("getPhoneNum"));

        int authNum = SendSMSTwilio.sendSMS("82", (String) inputMap.get("getPhoneNum"));

        // jwt??? ???????????? ???????????? ??????, ????????? ????????? ?????? ??? ???????????? ??????(jwt ?????? ??? ??????)
        Map<String, String> jwtToken = new HashMap<>();
        jwtToken.put("putPhoneNum", (String) inputMap.get("getPhoneNum"));
//        jwtToken.put("authenticationNum", String.valueOf(authNum));

        String phoneNumPlusAuthNum = (String)inputMap.get("getPhoneNum") + "_" + authNum;
        log.info(bCryptPasswordEncoder.encode(phoneNumPlusAuthNum));
        session.setAttribute("authNum", bCryptPasswordEncoder.encode(phoneNumPlusAuthNum));
        session.setMaxInactiveInterval(60*3);

        return jwtToken;
    }




    //2022-0513 ?????????
    //???????????? ?????? ??????
    @PostMapping("/signUp/authNumCorrect")
    @ResponseBody
    public  Map<String, String> certificationNum(@RequestBody Map<String, Object> inputMap, HttpServletRequest request){

        Map<String, String> returnMap = new HashMap<>();
        HttpSession session = request.getSession();

        String phoneNumPlusAuthNum = (String)inputMap.get("getPhoneNum") + "_" + inputMap.get("getphoneCertificationNum");

        log.info("???????????? ????????????: " + session.getAttribute("authNum"));
        log.info("???????????? ????????????: " + bCryptPasswordEncoder.matches(phoneNumPlusAuthNum , String.valueOf(session.getAttribute("authNum"))));

        if (bCryptPasswordEncoder.matches(phoneNumPlusAuthNum , String.valueOf(session.getAttribute("authNum")))){
            returnMap.put("isCertificationNumMatch", "true");
        } else{
            returnMap.put("isCertificationNumMatch", "false");
        }

        return returnMap;
    }






    @GetMapping("/accountInfo/{selectedTab}")
    public String showJoinedParties(@PathVariable String selectedTab, Model model, Principal principal) throws IOException {

        Member loginMember = memberRepository.findByUsername(principal.getName());
//        List<Party> ongoingParties = new ArrayList<>();
//        List<Party> closedParties = new ArrayList<>();

//        FileReader reader = new FileReader("src/main/resources/templates/account.html");
//
//        int ch;
//        while ((ch = reader.read()) != -1) {
//            System.out.print((char) ch);
//        }

        List<String> tabs = new ArrayList<>(Arrays.asList("myParties", "notification", "profile"));

        if (tabs.contains(selectedTab)){
            model.addAttribute("selectedTab", selectedTab);
        }   else{
            return "errorPage";
        }

        model.addAttribute("member", loginMember);
        model.addAttribute("notificationLogs", notificationLogRepository.findByMemberOrderByCreateAtDesc(loginMember));

        List<Object> myParties = memberService.DivideIntoClosedAndOngoingParties(memberPartyRepository.findByMember(loginMember));
        model.addAttribute("ongoingParties", (List<Party>) myParties.get(0));
        model.addAttribute("closedParties", (List<Party>) myParties.get(1));
        model.addAttribute("baseLocationChangeDto", new BaseLocationChangeDto());
        return "account";
    }






    @PostMapping("/accountInfo/basePickupLocation")
    public String ChangeBasePickupLocation(@Validated @ModelAttribute BaseLocationChangeDto baseLocationChangeDto, BindingResult bindingResult, Principal principal, Model model){

        Member member = memberRepository.findByUsername(principal.getName());


        basePickupLocationChangeValidator.validate(baseLocationChangeDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            model.addAttribute("selectedTab", "profile");

            model.addAttribute("member", member);
            model.addAttribute("notificationLogs", notificationLogRepository.findByMemberOrderByCreateAtDesc(member));

            List<Object> myParties = memberService.DivideIntoClosedAndOngoingParties(memberPartyRepository.findByMember(member));
            model.addAttribute("ongoingParties", (List<Party>) myParties.get(0));
            model.addAttribute("closedParties", (List<Party>) myParties.get(1));

            return "account";
        }




        memberService.ChangeBaseLocationAndLatitudeAndLongitude(member, baseLocationChangeDto.getChangedBaseLocation());

        notificationLogService.newNotificationLog(member,
                "?????? ??????????????? '" + baseLocationChangeDto.getChangedBaseLocation() + "'?????? ?????????????????????.", "/accountInfo/profile");

        return "redirect:/accountInfo/profile";
    }




}
