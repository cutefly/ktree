package kr.co.kpcard.ktree.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 프로젝트기여도
 * 
 * @author chris
 *
 */
@Getter
@Setter
public class ProjectScore {
	private int seq;

	/**
	 * 평가월(yyyyMM)
	 */
	private String month;
	/**
	 * 피평가자 사번
	 */
	private String employeId;

	/**
	 * 캐시박스 스코어
	 */
	private float score1;
	/**
	 * POSA 스코어
	 */
	private float score2;
	/**
	 * POP 스코어
	 */
	private float score3;
	/**
	 * Palrago 스코어
	 */
	private float score4;
	/**
	 * T-Grid 스코어
	 */
	private float score5;
	/**
	 * Topping 스코어
	 */
	private float score6;
	/**
	 * 관리(입력) 스코어
	 */
	private float score7;
	/**
	 * 관리(기본) 스코어
	 */
	private float score8;
	/**
	 * 이의제기
	 */
	private String dissent;
	/**
	 * 이의제기 1차 평가자 코멘트
	 */
	private String comments1;
	/**
	 * 이의제기 2차 평가자 코멘트
	 */
	private String comments2;
	/**
	 * 프로젝트 기여도 설정 상태 : 0 => 미설정, 1 => 본인설정완료, 2 => 1차평가자설정완료, 3 => 2차 평가자 설정완료
	 */
	private int status;
	/**
	 * 프로젝트 기여도 확인 스텝 : 0 => 본인설정, 1 => 1차평가자 컨펌, 2 => 2차 평가자 컨펌
	 */
	private int confirmNumber;
	private String confirm1;
	private String confirm2;

	public ProjectScore() {
	}

	public ProjectScore(String month, String employeId, float score1, float score2, float score3, float score4,
			float score5,
			float score6, float score7, float score8, String dissent, String comments1, String comments2, int status,
			int confirmNumber, String confirm1, String confirm2) {
		super();
		this.month = month;
		this.employeId = employeId;
		this.score1 = score1;
		this.score2 = score2;
		this.score3 = score3;
		this.score4 = score4;
		this.score5 = score5;
		this.score6 = score6;
		this.score7 = score7;
		this.score8 = score8;
		this.dissent = dissent;
		this.comments1 = comments1;
		this.comments2 = comments2;
		this.status = status;
		this.confirmNumber = confirmNumber;
		this.confirm1 = confirm1;
		this.confirm2 = confirm2;
	}
	public float getScore(int k) {
		switch (k) {
			case 1: return score1;
			case 2: return score2;
			case 3: return score3;
			case 4: return score4;
			case 5: return score5;
			case 6: return score6;
			case 7: return score7;
			case 8: return score8;
			default: return 0;
		}
	}

}
