package com.toyproject.share_deliveryfee_service.web.party;

import com.toyproject.share_deliveryfee_service.web.domain.MemberParty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPartyRepository extends JpaRepository<MemberParty, Long> {
}
