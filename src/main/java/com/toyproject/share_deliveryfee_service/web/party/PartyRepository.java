package com.toyproject.share_deliveryfee_service.web.party;

import com.toyproject.share_deliveryfee_service.web.domain.Member;
import com.toyproject.share_deliveryfee_service.web.domain.Party;
import com.toyproject.share_deliveryfee_service.web.domain.PartyStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PartyRepository extends JpaRepository<Party, Long> {




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




//(p.title like %:searchWord% or cast(p.id as string) = searchWord)