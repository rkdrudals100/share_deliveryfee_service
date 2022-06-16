package com.toyproject.share_deliveryfee_service.web.party;

import com.toyproject.share_deliveryfee_service.web.domain.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PartyMessageRepository extends JpaRepository<PartyMessage, Long> {

    Optional<PartyMessage> findById(Long id);

    List<PartyMessage> findByPartyAndAndProcessingStatus(Party party, ProcessingStatus processingStatus);

    List<PartyMessage> findByPartyAndProcessingStatusAndTypeOfMessageNotLike(Party party, ProcessingStatus processingStatus, TypeOfMessage typeOfMessage);

}
