package kr.co.kpcard.ktree.domain;

import lombok.Data;

@Data
public class TeamInfo {

	/**
	 * 팀코드
	 */
	private int code;

	/**
	 * 팀명
	 */
	private String name;

	/**
	 * 부서코드
	 */
	private int divisionCode;

	/**
	 * 부서명
	 */
	private String divisionName;
}
