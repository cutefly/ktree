package kr.co.kpcard.ktree.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerformanceValue {
	/* Employe */
	private String employeId;
	private String employeName;
	private String divisionName;
	private String teamName;
	private int authLevel;

	/* Perfomance */
	private int perfomScore1;
	private int perfomScore2;
	private String dissent;
	private String performComments1;
	private String performComments2;
	private String dissentComments1;
	private String dissentComments2;
	private String month;

	/* Value */
	private List<Boolean> valueScore1;
	private List<Boolean> valueScore2;
	private String valueComments1;
	private String valueComments2;

	private double valueRatio;
	private double performRatio;

	private String confirm1;
	private String confirm2;

	public void setEmployeValues(String employeId, String employeName, String divisionName, String teamName,
			int authLevel, String confirm1, String confirm2) {
		this.employeId = employeId;
		this.employeName = employeName;
		this.divisionName = divisionName;
		this.teamName = teamName;
		this.authLevel = authLevel;
		this.confirm1 = confirm1;
		this.confirm2 = confirm2;
	}

	public void setPerfomValues(int perfomScore1, int perfomScore2, String month) {
		this.perfomScore1 = perfomScore1;
		this.perfomScore2 = perfomScore2;
		this.month = month;
	}

	public void setRatio(double performRatio, double valueRatio) {
		this.performRatio = performRatio;
		this.valueRatio = valueRatio;
	}

}
