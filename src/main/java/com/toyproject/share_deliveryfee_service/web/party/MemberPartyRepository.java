package com.toyproject.share_deliveryfee_service.web.party;

import com.toyproject.share_deliveryfee_service.web.domain.Member;
import com.toyproject.share_deliveryfee_service.web.domain.MemberParty;
import com.toyproject.share_deliveryfee_service.web.domain.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberPartyRepository extends JpaRepository<MemberParty, Long> {


    List<MemberParty> findByMember(Member member);




    List<MemberParty> findByParty(Party party);
}
