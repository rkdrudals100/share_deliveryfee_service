package com.toyproject.share_deliveryfee_service.web.domain;

import lombok.Data;

import javax.persistence.*;

//  2022-04-14 강경민
//  도메인 설계
@Entity
@Data @Table(name = "food_categories")
public class FoodCategory extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "food_category_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private FoodCategoryName foodCategoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;
}
