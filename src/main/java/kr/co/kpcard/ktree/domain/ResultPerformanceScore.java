
package kr.co.kpcard.ktree.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPerformanceScore {
	private String employeId;
	private String month;
	private String employeName;
	private int authLevel;
	private String divisionName;
	private int divisionCode;
	private String teamName;
	private int teamCode;
	private int score1;
	private int score2;
	private String dissent;
	private String comments1;
	private String comments2;
	private String dissentComments1;
	private String dissentComments2;
	private String confirm1;
	private String confirm2;
}
