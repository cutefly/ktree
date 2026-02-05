package kr.co.kpcard.ktree.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 임직원정보
 * 
 * @author chris
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employe {
	/**
	 * 일련번호
	 */
	private Long seq;
	/**
	 * 사번
	 */
	private String employeId;
	/**
	 * 성명
	 */
	private String name;
	/**
	 * 비밀번호
	 */
	private String pwd;
	/**
	 * 권한코드
	 */
	private String authorityCode;
	/**
	 * 권한레벨(0: 사원, 1: 1차평가자, 2: 2차평가자, 3: 대표이사, 4: 관리자)
	 */
	private int authLevel;
	/**
	 * 생성일
	 */
	private java.time.LocalDateTime createDate;
	/**
	 * 사용여부(N: 퇴사)
	 */
	private String useYn;
	/**
	 * 팀코드
	 */
	private int teamCode;
	/**
	 * 부서코드
	 */
	private int divisionCode;
	/**
	 * 직급(0: 직원, 1: 팀장, 2: 본부장, 3: 임원)
	 */
	private int position;
	/**
	 * 컨펌1
	 */
	private String confirm1;
	/**
	 * 컨펌2
	 */
	private String confirm2;
	/**
	 * 모드
	 */
	private String mode;

}
