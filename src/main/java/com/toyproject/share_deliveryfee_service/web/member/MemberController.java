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

        notificationLogService.newNotificationLog(saveMember,
                "가입이 완료되었습니다.", "/accountInfo/selectedTab/profile");
        notificationLogService.newNotificationLog(saveMember,
                "기본 픽업장소를 등록해주세요. 검색할 때 기본 픽업 장소를 기준으로 가까운 파티부터 검색이 가능합니다.", "/accountInfo/selectedTab/profile");

        return "redirect:/login"; // 임시 작동 테스트
    }






    //아이디 중복 확인 기능 작성
    @GetMapping("/signUp/userId")
    @ResponseBody
    public Map<String, String> dupCheckMemberId(@RequestParam String userId){

        Map<String, String> returnMap = new HashMap<>();

        return memberService.DupCheckByUserId(userId, returnMap);
    }





    //문자 전송 버튼 작동 작업 및 검증 작성
    @PostMapping("/signUp/authNum")
    @ResponseBody
    public Map<String, String> sendAuthenticationNum(@RequestBody Map<String, Object> inputMap, HttpSession session){
        log.info("'{}' 로 인증번호 전송 시도", inputMap.get("getPhoneNum"));

        int authNum = SendSMSTwilio.sendSMS("82", (String) inputMap.get("getPhoneNum"));

        // jwt를 만들어서 유저에게 전송, 임시로 휴대폰 번호 및 인증번호 전송(jwt 공부 후 추가)
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("putPhoneNum", (String) inputMap.get("getPhoneNum"));
        returnMap.put("authenticationNum", String.valueOf(authNum));

        String phoneNumPlusAuthNum = (String)inputMap.get("getPhoneNum") + "_" + authNum;
        log.info(bCryptPasswordEncoder.encode(phoneNumPlusAuthNum));
        session.setAttribute("authNum", bCryptPasswordEncoder.encode(phoneNumPlusAuthNum));
        session.setMaxInactiveInterval(60*3);

        return returnMap;
    }





    //인증하기 버튼 작업
    @PostMapping("/signUp/phoneNum/authNum")
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






    @GetMapping("/accountInfo/selectedTab/{selectedTab}")
    public String showJoinedParties(@PathVariable String selectedTab, Model model, Principal principal) throws IOException {

        Member loginMember = memberRepository.findByUsername(principal.getName());

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






    @PatchMapping("/accountInfo/selectedTab/profile/basePickupLocation")
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
            model.addAttribute("baseLocationChangeDto", new BaseLocationChangeDto());

            return "account";
        }




        memberService.ChangeBaseLocationAndLatitudeAndLongitude(member, baseLocationChangeDto.getChangedBaseLocation());

        notificationLogService.newNotificationLog(member,
                "기본 픽업장소가 '" + baseLocationChangeDto.getChangedBaseLocation() + "'으로 변경되었습니다.", "/accountInfo/selectedTab/profile");

        return "redirect:/accountInfo/selectedTab/profile";
    }




}
