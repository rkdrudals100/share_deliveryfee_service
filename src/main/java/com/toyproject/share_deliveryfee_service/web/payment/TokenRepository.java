package com.toyproject.share_deliveryfee_service.web.payment;

import com.toyproject.share_deliveryfee_service.web.domain.Token;
import com.toyproject.share_deliveryfee_service.web.domain.enums.TypeOfToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findTokenByTypeOfToken(TypeOfToken typeOfToken);
}
