package kr.co.kpcard.ktree.domain;

/**
 * 가치평가
 * 
 * @author chris
 *
 */
public class ValueScore {
	/**
	 * 시퀀스
	 */
	private int seq;
	/**
	 * 평가월(yyyyMM)
	 */
	private String month;
	/**
	 * 사번
	 */
	private String employeId;
	/**
	 * 판단력평가
	 */
	private int score1;
	/**
	 * 소통평가
	 */
	private int score2;
	/**
	 * 임팩트평가
	 */
	private int score3;
	/**
	 * 호기심평가
	 */
	private int score4;
	/**
	 * 혁신평가
	 */
	private int score5;
	/**
	 * 열정평가
	 */
	private int score6;
	/**
	 * 정직평가
	 */
	private int score7;
	/**
	 * 이타심평가
	 */
	private int score8;
	/**
	 * 평가자 코멘트
	 */
	private String comments;
	/**
	 * 평가차수
	 */
	private int confirmNumber;

	public ValueScore() {
	}

	public void setValueScore(String month, String employeId, int score1, int score2, int score3, int score4,
			int score5,
			int score6, int score7, int score8, String comments, int confirmNumber) {
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
		this.comments = comments;
		this.confirmNumber = confirmNumber;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getEmployeId() {
		return employeId;
	}

	public void setEmployeId(String employeId) {
		this.employeId = employeId;
	}

	public int getScore1() {
		return score1;
	}

	public void setScore1(int score1) {
		this.score1 = score1;
	}

	public int getScore2() {
		return score2;
	}

	public void setScore2(int score2) {
		this.score2 = score2;
	}

	public int getScore3() {
		return score3;
	}

	public void setScore3(int score3) {
		this.score3 = score3;
	}

	public int getScore4() {
		return score4;
	}

	public void setScore4(int score4) {
		this.score4 = score4;
	}

	public int getScore5() {
		return score5;
	}

	public void setScore5(int score5) {
		this.score5 = score5;
	}

	public int getScore6() {
		return score6;
	}

	public void setScore6(int score6) {
		this.score6 = score6;
	}

	public int getScore7() {
		return score7;
	}

	public void setScore7(int score7) {
		this.score7 = score7;
	}

	public int getScore8() {
		return score8;
	}

	public void setScore8(int score8) {
		this.score8 = score8;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getConfirmNumber() {
		return confirmNumber;
	}

	public void setConfirmNumber(int confirmNumber) {
		this.confirmNumber = confirmNumber;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

}
