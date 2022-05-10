package com.toyproject.share_deliveryfee_service.web.party;

import com.toyproject.share_deliveryfee_service.web.domain.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
