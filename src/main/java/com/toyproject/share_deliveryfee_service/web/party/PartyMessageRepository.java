package com.toyproject.share_deliveryfee_service.web.party;

import com.toyproject.share_deliveryfee_service.web.domain.Party;
import com.toyproject.share_deliveryfee_service.web.domain.PartyMessage;
import com.toyproject.share_deliveryfee_service.web.domain.ProcessingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartyMessageRepository extends JpaRepository<PartyMessage, Long> {

    Optional<PartyMessage> findById(Long id);

    List<PartyMessage> findByPartyAndAndProcessingStatus(Party party, ProcessingStatus processingStatus);
}
