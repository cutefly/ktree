package kr.co.kpcard.ktree.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 임직원정보
 * 
 * @author chris
 *
 */
@Getter
@Setter
public class Employe {
	/**
	 * 사번
	 */
	private String employeId;
	/**
	 * 성명
	 */
	private String employeName;
	/**
	 * 비밀번호
	 */
	private String password;
	/**
	 * 사용여부(N: 퇴사)
	 */
	private String useYn;
	/**
	 * 팀코드
	 */
	private int teamCode;
	/**
	 * 팀명
	 */
	private String teamName;
	/**
	 * 부서코드
	 */
	private int divisionCode;
	/**
	 * 부서명
	 */
	private String divisionName;
	/**
	 * 권한레벨(1: 사웝, 1: 1차평가자, 2: 2차평가자, 3: 대표이사, 4: 관리자)
	 */
	private int authLevel;
	/**
	 * 직급(0: 직원, 1: 팀장, 2: 본부장, 3: 임원)
	 */
	private int position;

	public Employe() {
	}
}
