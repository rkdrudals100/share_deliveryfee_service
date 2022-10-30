package com.toyproject.share_deliveryfee_service.web.party;

import com.toyproject.share_deliveryfee_service.web.domain.MemberParty;
import com.toyproject.share_deliveryfee_service.web.domain.Party;
import com.toyproject.share_deliveryfee_service.web.domain.enums.PartyStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PartyRepository extends JpaRepository<Party, Long> {


//    Optional<Party> findById(Long id);

    @EntityGraph(attributePaths = {"memberParties"})
    Party findPartyById(Long id);




    List<Party> findByMemberParties(MemberParty memberParty);




    List<Party> findByLimitTimeBeforeAndPartyStatus(LocalDateTime limitTime, PartyStatus partyStatus);



    @Query("select p from Party p where p.partyStatus = :partyStatus")
    List<Party> findByPartyStatus(@Param("partyStatus") PartyStatus partyStatus, Sort sort);



    @Query("select p from Party p where p.partyStatus = :partyStatus and " +
            "(p.title like %:searchWord% or " +
            "p.pickUpLocation like %:searchWord% or " +
            "p.pickUpLocationDetail like %:searchWord% or " +
            "p.restaurant like %:searchWord% or " +
            "p.introduction like %:searchWord%)")
    List<Party> findBySearch(@Param("partyStatus") PartyStatus partyStatus, @Param("searchWord") String word, Sort sort);


}



