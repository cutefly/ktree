
package kr.co.kpcard.ktree.domain;

import java.util.List;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@ToString
public class ResultProjectScore {

	private String employeId;
	private String employeName;
	private String divisionName;
	private int divisionCode;
	private int position;
	private String teamName;
	private int teamCode;
	private int employeLevel;

	private int seq;
	private String month;
	private float score1;
	private float score2;
	private float score3;
	private float score4;
	private float score5;
	private float score6;
	private float score7;
	private float score8;
	private int status;
	private String dissent;
	private String comments1;
	private String comments2;
	private int confirmNumber;
	private String confirm1;
	private String confirm2;

	// Calculator fields
	private boolean userAuth;
	private boolean userDissent;
	private int authLevel;
	private boolean evaluable; // 평가가능 여부
	private boolean dissentable; // 이의제기 가능 여부

	// Calculate score
	public float getTotalScore() {
		return score1 + score2 + score3 + score4 + score5 + score6 + score7 + score8;
	}

	public List<Float> getScores() {
		return List.of(score1, score2, score3, score4, score5, score6, score7, score8);
	}

	private List<Float> myScores;

	public void setEmploye(String employeId, String employeName, String divisionName, int divisionCode, int position,
			String teamName, int teamCode, int employeLevel) {
		this.employeId = employeId;
		this.employeName = employeName;
		this.divisionName = divisionName;
		this.divisionCode = divisionCode;
		this.position = position;
		this.teamName = teamName;
		this.teamCode = teamCode;
		this.employeLevel = employeLevel;
	}

	/*
	 * public void setProjectScore(int seq, String month, float score1, float
	 * score2, float score3, float score4,
	 * float score5, float score6, float score7, float score8, int status, String
	 * dissent, String comments1,
	 * String comments2, int confirmNumber, String confirm1, String confirm2) {
	 * this.seq = seq;
	 * this.month = month;
	 * this.score1 = score1;
	 * this.score2 = score2;
	 * this.score3 = score3;
	 * this.score4 = score4;
	 * this.score5 = score5;
	 * this.score6 = score6;
	 * this.score7 = score7;
	 * this.score8 = score8;
	 * this.status = status;
	 * this.dissent = dissent;
	 * this.comments1 = comments1;
	 * this.comments2 = comments2;
	 * this.confirmNumber = confirmNumber;
	 * this.confirm1 = confirm1;
	 * this.confirm2 = confirm2;
	 * }
	 */

}
