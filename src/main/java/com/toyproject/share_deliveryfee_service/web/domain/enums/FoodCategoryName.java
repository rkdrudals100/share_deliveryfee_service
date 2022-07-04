package com.toyproject.share_deliveryfee_service.web.domain.enums;

//  2022-04-14 강경민
//  도메인 설계
public enum FoodCategoryName {
    CHICKEN("치킨"), PIZZA("피자"), PORKFEET("족발보쌈"), KOREANFOOD("한식"), CHINESEFOOD("중식"), WESTERNFOOD("양식"), SNACKFOOD("분식");

    private final String name;

    FoodCategoryName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
