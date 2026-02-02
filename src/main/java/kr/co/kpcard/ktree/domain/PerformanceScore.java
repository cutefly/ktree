package kr.co.kpcard.ktree.domain;

/**
 * 성과평가
 * 
 * @author chris
 *
 */
public class PerformanceScore {

	/**
	 * 평가월(yyyyMM)
	 */
	private String month;
	/**
	 * 사번
	 */
	private String employeId;
	/**
	 * 점수
	 */
	private int score;
	/**
	 * 이의제기
	 */
	private String dissent;
	/**
	 * 평가자 코멘트
	 */
	private String comments;
	/**
	 * 이의제기 코멘트
	 */
	private String dissentComments;
	/**
	 * 평가 차수
	 */
	private int confirmNumber;

	public PerformanceScore() {
	}

	public void setPerformanceScore(String month, String employeId, String dissent, String comments,
			int confirmNumber) {

		this.month = month;
		this.employeId = employeId;
		this.dissent = dissent;
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getDissent() {
		return dissent;
	}

	public void setDissent(String dissent) {
		this.dissent = dissent;
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

	public String getDissentComments() {
		return dissentComments;
	}

	public void setDissentComments(String dissentComments) {
		this.dissentComments = dissentComments;
	}

}
