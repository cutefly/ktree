package kr.co.kpcard.ktree.domain;

import java.sql.Timestamp;

public record UserInfo(
        long seq,
        String employeId,
        String name,
        String pwd,
        String authorityCode,
        Timestamp createDate,
        String useYn,
        int teamCode,
        int divisionCode,
        int position,
        String confirm1,
        String confirm2) {
}
