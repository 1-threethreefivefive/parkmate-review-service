package com.parkmate.reviewservice.reviewreactions.domain;


import lombok.Getter;

@Getter
public enum ReactionType {

    NONE("없음"),
    LIKE("좋아요"),
    DISLIKE("싫어요");

    private final String description;

    ReactionType(String description) {
        this.description = description;
    }
}