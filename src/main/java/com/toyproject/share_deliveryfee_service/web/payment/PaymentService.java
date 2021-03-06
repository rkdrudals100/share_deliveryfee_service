package com.toyproject.share_deliveryfee_service.web.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyproject.share_deliveryfee_service.web.config.ConfigUtil;
import com.toyproject.share_deliveryfee_service.web.domain.Member;
import com.toyproject.share_deliveryfee_service.web.domain.Party;
import com.toyproject.share_deliveryfee_service.web.domain.Payments;
import com.toyproject.share_deliveryfee_service.web.domain.Token;
import com.toyproject.share_deliveryfee_service.web.domain.enums.ReasonForPayment;
import com.toyproject.share_deliveryfee_service.web.domain.enums.TypeOfToken;
import com.toyproject.share_deliveryfee_service.web.payment.form.ImportTokenRes;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentsRepository paymentsRepository;
    private final TokenRepository tokenRepository;

    private final ConfigUtil configUtil;







    public void newPayment(Party party, Member member,
                           String merchant_uid, int amount,
                           ReasonForPayment reasonForPayment){
        log.warn(party.getTitle());
        log.warn(member.getUsername());
        log.warn(merchant_uid);
        log.warn(String.valueOf(amount));
        log.warn(String.valueOf(reasonForPayment));

        Payments payments = Payments.builder()
                .merchantUid(merchant_uid)
                .reasonForPayment(reasonForPayment)
                .amount(amount)
                .refundAmount(0)
                .payer(member)
                .party(party)
                .build();

        paymentsRepository.save(payments);

        party.addPayments(payments);
        member.addPayments(payments);
    }









    public String getAccessToken(){

        Token iamportToken = tokenRepository.findTokenByTypeOfToken(TypeOfToken.IAMPORT);

        log.warn(String.valueOf(System.currentTimeMillis() / 1000));
        if (iamportToken == null || iamportToken.getExpired_at() < (System.currentTimeMillis() / 1000)){

            Map<String, Object> params2 = new HashMap<>();
            params2.put("imp_key", configUtil.getProperty("iamport_restAPI_key"));
            params2.put("imp_secret", configUtil.getProperty("iamport_restAPI_secret"));

            log.warn((String) params2.get("imp_key"));
            log.warn((String) params2.get("imp_secret"));

            HttpResponse<JsonNode> response = Unirest.post("https://api.iamport.kr/users/getToken")
                    .header("accept", "application/json")
                    .fields(params2)
                    .asJson();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            try {
                ImportTokenRes bodyJson = objectMapper.readValue(response.getBody().getObject().toString(), ImportTokenRes.class);
                if (bodyJson.getResponse() != null && bodyJson.getResponse().size() != 0) {

                    if (iamportToken != null){
                        tokenRepository.delete(tokenRepository.findTokenByTypeOfToken(TypeOfToken.IAMPORT));
                    }

                    Token newIamportToken = Token.builder()
                            .typeOfToken(TypeOfToken.IAMPORT)
                            .access_token(bodyJson.getResponse().get(0).getAccess_token())
                            .created_at((long) bodyJson.getResponse().get(0).getNow())
                            .expired_at((long) bodyJson.getResponse().get(0).getExpired_at())
                            .build();

                    tokenRepository.save(newIamportToken);

                    log.info("????????? ?????? ??????");
                    return bodyJson.getResponse().get(0).getAccess_token();
                }   else {
                    log.warn("???????????? ?????? ?????? ??? ?????? ??????");
                    return null;
                }
            } catch (JsonProcessingException e){
                log.warn("???????????? ?????? ?????? ?????? ?????? ??????");
                return null;
            }
        } else {
            return iamportToken.getAccess_token();
        }
    }









    public String cancelPayments(Payments payments){

        int cancelableAmount = payments.getAmount() - payments.getRefundAmount();

        if (cancelableAmount <= 0){
            return "noCancelableAmount";
        }

        String AccessToken = getAccessToken();
        log.warn("?????? " + AccessToken);

        String url = "https://api.iamport.kr/payments/cancel";

        Map<String, Object> params = new HashMap<>();
        params.put("merchant_uid", payments.getMerchantUid());
        params.put("amount", payments.getAmount());
        params.put("cancel_amount", cancelableAmount);

        HttpResponse<JsonNode> response = Unirest.post(url)
                .header("Authorization", AccessToken)
                .fields(params)
                .asJson();

        int codeNum = Integer.parseInt(response.getBody().toString().split(",")[0].split(":")[1]);

        if (codeNum == 0){
            log.warn("?????? ??????");
            return "cancelSuccess";
        } else if (codeNum == 1){
            log.warn("?????? ?????? ????????? ????????????.");
            return "alreadyCancel";
        } else if (codeNum == -1){
            log.warn("????????? ????????? ?????? ????????? ?????? ?????? ????????? ????????????.");
            return "alreadyCancel";
        } else {
            log.warn("??? ??? ?????? ????????? ?????? ??????");
            return "unknownError";
        }

    }


}
