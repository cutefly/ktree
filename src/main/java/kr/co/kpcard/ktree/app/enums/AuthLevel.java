package kr.co.kpcard.ktree.app.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthLevel {
    REVIEWEE(0, "피평가자"),
    PRIMARY_REVIEWER(1, "1차평가자"),
    SECONDARY_REVIEWER(2, "2차평가자"),
    CEO(3, "대표이사"),
    ADMIN(4, "관리자");

    private final int code;
    private final String description;

    public static AuthLevel fromCode(int code) {
        for (AuthLevel authLevel : AuthLevel.values()) {
            if (authLevel.getCode() == code) {
                return authLevel;
            }
        }
        throw new IllegalArgumentException("Invalid AuthLevel code: " + code);
    }
}
