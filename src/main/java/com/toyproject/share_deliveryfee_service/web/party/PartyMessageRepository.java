package com.toyproject.share_deliveryfee_service.web.party;

import com.toyproject.share_deliveryfee_service.web.domain.*;
import com.toyproject.share_deliveryfee_service.web.domain.enums.ProcessingStatus;
import com.toyproject.share_deliveryfee_service.web.domain.enums.TypeOfMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartyMessageRepository extends JpaRepository<PartyMessage, Long> {

    Optional<PartyMessage> findById(Long id);

    List<PartyMessage> findByPartyAndAndProcessingStatusOrderByIdDesc(Party party, ProcessingStatus processingStatus);

    List<PartyMessage> findByPartyAndProcessingStatusAndTypeOfMessageNotLikeOrderByIdDesc(Party party, ProcessingStatus processingStatus, TypeOfMessage typeOfMessage);

}
