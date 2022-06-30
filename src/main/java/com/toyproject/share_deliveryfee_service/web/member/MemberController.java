package com.toyproject.share_deliveryfee_service.web.member;

import com.toyproject.share_deliveryfee_service.web.domain.*;
import com.toyproject.share_deliveryfee_service.web.member.form.MemberDupCheckIdDto;
import com.toyproject.share_deliveryfee_service.web.member.form.MemberRegisterDto;
import com.toyproject.share_deliveryfee_service.web.member.validator.MemberDupCheckValidator;
import com.toyproject.share_deliveryfee_service.web.member.validator.MemberRegisterValidator;
import com.toyproject.share_deliveryfee_service.web.notificationLog.NotificationLogRepository;
import com.toyproject.share_deliveryfee_service.web.notificationLog.NotificationLogService;
import com.toyproject.share_deliveryfee_service.web.party.MemberPartyRepository;
import com.toyproject.share_deliveryfee_service.web.party.PartyRepository;
import com.toyproject.share_deliveryfee_service.web.sms.SendSMSTwilio;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileReader;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberRegisterValidator memberRegisterValidator;
    private final MemberDupCheckValidator memberDupCheckValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MemberRepository memberRepository;
    private final MemberPartyRepository memberPartyRepository;
    private final PartyRepository partyRepository;
    private final NotificationLogRepository notificationLogRepository;

    private final MemberService memberService;
    private final NotificationLogService notificationLogService;






    @GetMapping("admin/hello")
    @ResponseBody
    public String admin(){
        return "<h1>admin</h1>";
    }




    @GetMapping("/login")
    public String login(Model model, Principal principal){
        if(principal != null) {
            return "redirect:/";
        }
        return "login";
    }




    @GetMapping("/signUp")
    public String signUp(Model model) {

        model.addAttribute("member", new MemberRegisterDto());
        return "signUp";
    }







    //2022-04-21 강경민
    //멤버 저장 시 검증 작성
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
            log.info("인증번호오류");

            return "signUp";
        }
        session.removeAttribute(phoneNumPlusAuthNum);

        Member saveMember = memberService.registerMember(memberRegisterDto.getMemberId(), bCryptPasswordEncoder.encode(memberRegisterDto.getMemberPassword()),
                memberRegisterDto.getMemberEmail(), memberRegisterDto.getMemberPhoneNum(), MemberRole.USER, "ROLE_USER");

        log.info("'{}'을 member에 저장 완료", memberRegisterDto.getMemberId());

        notificationLogService.newNotificationLog(saveMember, "가입이 완료되었습니다.", "/accountInfo/profile");

        return "redirect:/login"; // 임시 작동 테스트
    }






    @PostMapping("/signUp/dupCheckId2")
    public String dupFormTest(@Validated @ModelAttribute("member") MemberDupCheckIdDto memberDupCheckIdDto, BindingResult bindingResult){

//        log.info("중복체크 버튼 클릭 후 컨트롤러 진입");
//        log.info("{}", memberDupCheckIdDto.toString());
//        log.info("{}", memberDupCheckIdDto.getMemberId());

        memberDupCheckValidator.validate(memberDupCheckIdDto, bindingResult);

//        log.info("검증기 실행 완료");
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            return "signUp";
        }

        return "redirect:/signUp";
    }






    //2022-0422 강경민
    //아이디 중복 확인 기능 작성
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




    //2022-0422 강경민
    //문자 전송 버튼 작동 작업 및 검증 작성
    @PostMapping("/signUp/sendAuthenticationNum")
    @ResponseBody
    public Map<String, String> sendAuthenticationNum(@RequestBody Map<String, Object> inputMap, HttpSession session){
        log.info("'{}' 로 인증번호 전송 시도", inputMap.get("getPhoneNum"));

        int authNum = SendSMSTwilio.sendSMS("82", (String) inputMap.get("getPhoneNum"));

        // jwt를 만들어서 유저에게 전송, 임시로 휴대폰 번호 및 인증번호 전송(jwt 공부 후 추가)
        Map<String, String> jwtToken = new HashMap<>();
        jwtToken.put("putPhoneNum", (String) inputMap.get("getPhoneNum"));
//        jwtToken.put("authenticationNum", String.valueOf(authNum));

        String phoneNumPlusAuthNum = (String)inputMap.get("getPhoneNum") + "_" + authNum;
        log.info(bCryptPasswordEncoder.encode(phoneNumPlusAuthNum));
        session.setAttribute("authNum", bCryptPasswordEncoder.encode(phoneNumPlusAuthNum));
        session.setMaxInactiveInterval(60*3);

        return jwtToken;
    }




    //2022-0513 강경민
    //인증하기 버튼 작업
    @PostMapping("/signUp/authNumCorrect")
    @ResponseBody
    public  Map<String, String> certificationNum(@RequestBody Map<String, Object> inputMap, HttpServletRequest request){

        Map<String, String> returnMap = new HashMap<>();
        HttpSession session = request.getSession();

        String phoneNumPlusAuthNum = (String)inputMap.get("getPhoneNum") + "_" + inputMap.get("getphoneCertificationNum");

        log.info("암호화된 인증번호: " + session.getAttribute("authNum"));
        log.info("인증번호 일치여부: " + bCryptPasswordEncoder.matches(phoneNumPlusAuthNum , String.valueOf(session.getAttribute("authNum"))));

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
        return "account";
    }




}
