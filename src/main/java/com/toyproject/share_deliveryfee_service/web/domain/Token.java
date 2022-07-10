package com.toyproject.share_deliveryfee_service.web.domain;

import com.toyproject.share_deliveryfee_service.web.domain.enums.TypeOfToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @Table(name = "tokens")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    @Id @GeneratedValue
    @Column(name = "token_id")
    private Long id;

    private TypeOfToken typeOfToken;

    private String access_token;

    private Long created_at;

    private Long expired_at;
}
