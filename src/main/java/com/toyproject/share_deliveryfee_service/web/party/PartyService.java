package com.toyproject.share_deliveryfee_service.web.party;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyproject.share_deliveryfee_service.web.config.ConfigUtil;
import com.toyproject.share_deliveryfee_service.web.domain.*;
import com.toyproject.share_deliveryfee_service.web.member.MemberRepository;
import com.toyproject.share_deliveryfee_service.web.party.form.KakaoGeoRes;
import com.toyproject.share_deliveryfee_service.web.party.form.PartyRegisterDto;
import com.toyproject.share_deliveryfee_service.web.party.form.PartySearchDto;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PartyService {

    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;
    private final MemberPartyRepository memberPartyRepository;

    private final ConfigUtil configUtil;

    
    // 2022-05-19 강경민
    // 파티 저장 기능 구현
    public Party createParty(String username, PartyRegisterDto partyRegisterDto){

        Member member = memberRepository.findByUsername(username);

        DeliveryPlatform getDeliveryPlatform;

        if(partyRegisterDto.getDeliveryPlatform() == 0){
            getDeliveryPlatform = DeliveryPlatform.BM;
        } else if(partyRegisterDto.getDeliveryPlatform() == 1){
            getDeliveryPlatform = DeliveryPlatform.YGY;
        } else if(partyRegisterDto.getDeliveryPlatform() == 2){
            getDeliveryPlatform = DeliveryPlatform.BT;
        } else {
            getDeliveryPlatform = DeliveryPlatform.BM;
        }

        Map<String, Double> latitudeAndLongitude = getLatitudeAndLongitudeFromKakaoMap(partyRegisterDto.getPickUpLocation());

        Party saveParty = Party.builder()
                .title(partyRegisterDto.getTitle())
                .pickUpLocation(partyRegisterDto.getPickUpLocation())
                .pickUpLocationDetail(partyRegisterDto.getPickUpLocationDetail())
                .latitude(latitudeAndLongitude.get("latitude"))
                .longitude(latitudeAndLongitude.get("longitude"))
                .restaurant(partyRegisterDto.getRestaurant())
                .introduction(partyRegisterDto.getIntroduction())
                .totalPrice(partyRegisterDto.getTotalPrice())
                .membersNum(1)
                .maxMemberNum(partyRegisterDto.getMaxMemberNum())
                .limitTime(partyRegisterDto.getLimitTime())
                .deliveryPlatform(getDeliveryPlatform)
                .partyStatus(PartyStatus.RECRUITING)
                .build();

        saveParty.changeOrganizer(member);


        MemberParty saveMemberParty = MemberParty.builder()
                .member(member)
                .party(saveParty)
                .build();

        memberPartyRepository.save(saveMemberParty);

        return partyRepository.save(saveParty);
    }





    public int changePageContents(int currentPageNum,  int totalPageNum,  String whichBtn){
        int printCardNum = 0;

        if (whichBtn.equals("right")) {
            if ((currentPageNum) * 6 <= totalPageNum) {
                printCardNum = 6;
            } else {
                printCardNum = 6 - ((currentPageNum * 6) - totalPageNum);
            }
        }else{
            printCardNum = 6;
        }

        return printCardNum;
    }





    // 파티 검색
    public List<Party> searchPartyByKeywords(String keyWord){
        String[] keywords = keyWord.split("\\s", 2);
        List<Party> parties = new ArrayList<>();

        if (keywords.length > 1){
            List<Party> parties1 = partyRepository.findBySearch(PartyStatus.RECRUITING, keywords[0], Sort.by(Sort.Direction.ASC, "pickUpLocation"));
            List<Party> parties2 = partyRepository.findBySearch(PartyStatus.RECRUITING, keywords[1], Sort.by(Sort.Direction.ASC, "pickUpLocation"));

            for (Party party: parties1){
                if (parties2.contains(party)){
                    parties.add(party);
                }
            }
        } else {
            parties = partyRepository.findBySearch(PartyStatus.RECRUITING, keyWord, Sort.by(Sort.Direction.ASC, "pickUpLocation"));
        }

        return parties;
    }





    // 유저가 파티에 가입했는지, 파티원인지, 파티장인지 판별
    public String determineUserRoleAtParty(Member member, Party party){
        String userRole = "notJoined";

        List<MemberParty> memberJoinedParty = memberPartyRepository.findByMember(member);
        List<MemberParty> partyMembers = memberPartyRepository.findByParty(party);

        int sizeOfJoinedParty = memberJoinedParty.size();
        memberJoinedParty.removeAll(partyMembers);
        int sizeOfJoinedParty2 = memberJoinedParty.size();

        if(sizeOfJoinedParty == sizeOfJoinedParty2){
            return userRole;
        }   else {
            userRole = "joined";
        }

        if (party.getOrganizer() == member){
            userRole = "organizer";
        }

        return userRole;
    }




    public PartyStatus updatePartyStatus(Party party, String partyDescription){

        PartyStatus findPartyStatus = PartyStatus.RECRUITING;

        if (partyDescription.equals("모집 중")){
            findPartyStatus = PartyStatus.RECRUITING;
        } else if (partyDescription.equals("모집 완료")){
            findPartyStatus = PartyStatus.RECCOMPLETED;
        } else if (partyDescription.equals("주문 완료")){
            findPartyStatus = PartyStatus.ORDERED;
        } else if (partyDescription.equals("배달 완료")){
            findPartyStatus = PartyStatus.DELCOMPLETED;
        }

        return party.updatePartyStatus(findPartyStatus);
    }





    public double distanceInKilometerByHaversine(double lat1, double lon1, double lat2, double lon2) {

        double R = 6372.8; // 지구 반지름(km)

        double deltaLatitude = Math.toRadians(lat2 - lat1);
        double deltaLongitude = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(deltaLatitude / 2),2) + Math.pow(Math.sin(deltaLongitude / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return R * c;
    }







    public Map<String, Double> getLatitudeAndLongitudeFromKakaoMap(String location){

        Map<String, Double> returnMap = new HashMap<>();

        String restAPI_key = configUtil.getProperty("restAPI_key");
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=";

        HttpResponse<JsonNode> response = Unirest.get(url + location)
                .header("Authorization", "KakaoAK " + restAPI_key)
                .asJson();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);


        try {
            KakaoGeoRes bodyJson = objectMapper.readValue(response.getBody().getObject().toString(), KakaoGeoRes.class);

            if (bodyJson.getDocuments() != null && bodyJson.getDocuments().size() != 0) {
                returnMap.put("latitude", bodyJson.getDocuments().get(0).getX());
                returnMap.put("longitude", bodyJson.getDocuments().get(0).getY());
            } else {
                return null;
            }

            return returnMap;

        } catch (JsonProcessingException e) {
            return null;
//            e.printStackTrace();
        }
    }





    public List<Party> sortByDistanceFromUser(List<Party> parties){



        return null;
    }







    public List<PartySearchDto> calculateAndAddDistance(Member member, List<Party> parties){
        
        List<PartySearchDto> partySearchDtos = new ArrayList<>();

        for (Party party : parties){
            PartySearchDto partySearchDto = PartySearchDto.builder()
                    .id(party.getId())
                    .title(party.getTitle())
                    .introduction(party.getIntroduction())
                    .pickUpLocation(party.getPickUpLocation())
                    .pickUpLocationDetail(party.getPickUpLocationDetail())
                    .latitude(party.getLatitude())
                    .longitude(party.getLongitude())
                    .restaurant(party.getRestaurant())
                    .totalPrice(party.getTotalPrice())
                    .membersNum(party.getMembersNum())
                    .maxMemberNum(party.getMaxMemberNum())
                    .limitTime(party.getLimitTime())
                    .deliveryPlatform(party.getDeliveryPlatform())
                    .partyStatus(party.getPartyStatus())
                    .organizer(party.getOrganizer())
                    .build();


            double distance = distanceInKilometerByHaversine(partySearchDto.getLatitude(), partySearchDto.getLongitude(),
                    member.getLatitude(), member.getLongitude());

            partySearchDto.calcDistanceFromMemberBaseLocation(distance);

            partySearchDtos.add(partySearchDto);
        }

        return partySearchDtos;
    }


}
