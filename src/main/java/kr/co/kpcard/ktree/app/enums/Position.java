package kr.co.kpcard.ktree.app.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Position {
    EMPLOYEE(0, "사원"),
    TEAM_LEADER(1, "팀장"),
    DEPARTMENT_HEAD(2, "본부장"),
    EXECUTIVE(3, "임원");

    private final int code;
    private final String description;

    public static Position fromCode(int code) {
        for (Position position : Position.values()) {
            if (position.getCode() == code) {
                return position;
            }
        }
        throw new IllegalArgumentException("Invalid Position code: " + code);
    }
}
