package kr.co.kpcard.ktree.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.kpcard.ktree.domain.DivisionInfo;
import kr.co.kpcard.ktree.domain.Employe;
import kr.co.kpcard.ktree.domain.PerformanceScore;
import kr.co.kpcard.ktree.domain.PerformanceValue;
import kr.co.kpcard.ktree.dao.PersonalEvaluationDao;
import kr.co.kpcard.ktree.domain.ProjectScore;
import kr.co.kpcard.ktree.domain.ResultPerformanceScore;
import kr.co.kpcard.ktree.domain.ResultProjectScore;
import kr.co.kpcard.ktree.domain.ResultValueScore;
import kr.co.kpcard.ktree.domain.TeamInfo;
import kr.co.kpcard.ktree.domain.ValueScore;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonalEvaluationService {

	protected final Logger logger = LoggerFactory.getLogger(PersonalEvaluationService.class);

	static final List<Integer> officer = new ArrayList<Integer>(); // 임원
	static final List<Integer> commandTeam = new ArrayList<Integer>(); // 관리본부
	static final List<Integer> tGridTeam = new ArrayList<Integer>(); // T-Grid 사업본부
	static final List<Integer> degitalTeam = new ArrayList<Integer>(); // Digital 사업본부
	static final List<Integer> distributeTeam = new ArrayList<Integer>(); // 유통사업본부
	static final List<Integer> developTeam = new ArrayList<Integer>(); // 기술개발연구소
	static final List<Integer> financeTeam = new ArrayList<Integer>(); // 재무본부

	private final PersonalEvaluationDao personalEvaluationDao;

	public HashMap<String, Object> getProjectScore(String employeId, String yyyyMM) {
		logger.info("getProjectScoreList | IN |");

		List<ResultProjectScore> resultList = new ArrayList<ResultProjectScore>();
		List<ProjectScore> resultHistoryList = new ArrayList<ProjectScore>();
		ResultProjectScore resultProjectScore = new ResultProjectScore();
		ProjectScore projectScore = personalEvaluationDao.getProjectScore(employeId, yyyyMM);
		ProjectScore projectScoreHistory = personalEvaluationDao.getProjectScoreHistory(employeId, yyyyMM);
		Employe employe = personalEvaluationDao.getEmploye(employeId);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		setRatio();
		float scoreRatio = 0;

		if (employe.getEmployeId().equals(projectScore.getEmployeId())) {
			if (projectScore.getStatus() > 0) { // 최초 1회 이상 프로젝트 기여도 설정이 되어 있는 경우{
				scoreRatio = projectScore.getScore8();
			} else {
				switch (employe.getDivisionCode()) {
					case 0:
						scoreRatio = officer.get(employe.getPosition());
						break;
					case 1:
						scoreRatio = commandTeam.get(employe.getPosition());
						break;
					case 2:
						scoreRatio = tGridTeam.get(employe.getPosition());
						break;
					case 3:
						scoreRatio = degitalTeam.get(employe.getPosition());
						break;
					case 4:
						scoreRatio = distributeTeam.get(employe.getPosition());
						break;
					case 5:
						scoreRatio = developTeam.get(employe.getPosition());
						break;
					case 6:
						scoreRatio = financeTeam.get(employe.getPosition());
						break;
				}
			}
			resultProjectScore = ResultProjectScore.builder()
					.seq(projectScore.getSeq())
					.month(projectScore.getMonth())
					.score1(projectScore.getScore1())
					.score2(projectScore.getScore2())
					.score3(projectScore.getScore3())
					.score4(projectScore.getScore4())
					.score5(projectScore.getScore5())
					.score6(projectScore.getScore6())
					.score7(projectScore.getScore7())
					.score8(scoreRatio)
					.status(projectScore.getStatus())
					.dissent(projectScore.getDissent())
					.comments1(projectScore.getComments1())
					.comments2(projectScore.getComments2())
					.confirmNumber(projectScore.getConfirmNumber())
					.confirm1(projectScore.getConfirm1())
					.confirm2(projectScore.getConfirm2())
					.build();
		}
		resultProjectScore.setEmploye(employe.getEmployeId(),
				employe.getEmployeName(),
				employe.getDivisionName(),
				employe.getDivisionCode(),
				employe.getPosition(),
				employe.getPosition() == 3 ? "임원"
						: ((employe.getDivisionCode() > 0 && employe.getTeamCode() == 0) ? "부서장"
								: employe.getTeamName()),
				employe.getTeamCode(),
				employe.getAuthLevel());

		resultList.add(resultProjectScore);
		resultHistoryList.add(projectScoreHistory);
		resultMap.put("resultProjectScore", resultList);
		resultMap.put("scoreHistory", resultHistoryList);

		logger.info("getProjectScoreList | OUT |");

		return resultMap;
	}

	public HashMap<String, Object> getProjectScoreAll(String employeId, String yyyyMM) {
		logger.info("getProjectScoreListALL | IN |");

		List<ResultProjectScore> resultList = new ArrayList<ResultProjectScore>();
		List<ProjectScore> projectScoreList = personalEvaluationDao.getProjectScoreAll(employeId, yyyyMM);
		List<ProjectScore> projectScoreHistoryList = personalEvaluationDao.getProjectScoreAllHistory(employeId, yyyyMM);
		List<Employe> employeList = personalEvaluationDao.getEmployeList();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		setRatio();
		float scoreRatio = 0;

		for (Employe employe : employeList) {

			ResultProjectScore resultProjectScore = new ResultProjectScore();

			for (ProjectScore projectScore : projectScoreList) {
				if (employe.getEmployeId().equals(projectScore.getEmployeId())) {
					if (projectScore.getStatus() > 0) // 최초 1회 이상 프로젝트 기여도 설정이 되어 있는 경우
					{
						scoreRatio = projectScore.getScore8();
					} else {
						switch (employe.getDivisionCode()) {
							case 0:
								scoreRatio = officer.get(employe.getPosition());
								break;
							case 1:
								scoreRatio = commandTeam.get(employe.getPosition());
								break;
							case 2:
								scoreRatio = tGridTeam.get(employe.getPosition());
								break;
							case 3:
								scoreRatio = degitalTeam.get(employe.getPosition());
								break;
							case 4:
								scoreRatio = distributeTeam.get(employe.getPosition());
								break;
							case 5:
								scoreRatio = developTeam.get(employe.getPosition());
								break;
							case 6:
								scoreRatio = financeTeam.get(employe.getPosition());
								break;
							default:
								break;
						}
					}
					resultProjectScore = ResultProjectScore.builder()
							.seq(projectScore.getSeq())
							.month(projectScore.getMonth())
							.score1(projectScore.getScore1())
							.score2(projectScore.getScore2())
							.score3(projectScore.getScore3())
							.score4(projectScore.getScore4())
							.score5(projectScore.getScore5())
							.score6(projectScore.getScore6())
							.score7(projectScore.getScore7())
							.score8(scoreRatio)
							.status(projectScore.getStatus())
							.dissent(projectScore.getDissent())
							.comments1(projectScore.getComments1())
							.comments2(projectScore.getComments2())
							.confirmNumber(projectScore.getConfirmNumber())
							.confirm1(projectScore.getConfirm1())
							.confirm2(projectScore.getConfirm2())
							.build();
				}
			}

			// history

			resultProjectScore.setEmploye(employe.getEmployeId(),
					employe.getEmployeName(),
					employe.getDivisionName(),
					employe.getDivisionCode(),
					employe.getPosition(),
					employe.getPosition() == 3 ? "임원"
							: ((employe.getDivisionCode() > 0 && employe.getTeamCode() == 0) ? "부서장"
									: employe.getTeamName()),
					employe.getTeamCode(),
					employe.getAuthLevel());

			resultList.add(resultProjectScore);
		}
		resultMap.put("resultProjectScore", resultList);
		resultMap.put("scoreHistory", projectScoreHistoryList);

		logger.debug("getProjectScoreList=> {}, EmployeList=> {}", projectScoreList.size(), employeList.size());
		logger.info("getProjectScoreListALL | OUT |");

		return resultMap;

	}

	public HashMap<String, Object> getProjectScoreDivision(String employeId, String yyyyMM, int divisionCode) {
		logger.info("getProjectScoreListDivision | IN |");

		List<ResultProjectScore> resultList = new ArrayList<ResultProjectScore>();
		List<ProjectScore> projectScoreList = personalEvaluationDao.getProjectScoreAll(employeId, yyyyMM);
		List<ProjectScore> projectScoreHistoryList = personalEvaluationDao.getProjectScoreAllHistory(employeId, yyyyMM);
		List<Employe> employeList = personalEvaluationDao.getEmployeList();

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		setRatio();
		float scoreRatio = 0;

		for (Employe employe : employeList) {
			if (employe.getDivisionCode() == divisionCode) {
				ResultProjectScore resultProjectScore = new ResultProjectScore();

				for (ProjectScore projectScore : projectScoreList) {
					if (employe.getEmployeId().equals(projectScore.getEmployeId())) {
						if (projectScore.getStatus() > 0) // 최초 1회 이상 프로젝트 기여도 설정이 되어 있는 경우
						{
							scoreRatio = projectScore.getScore8();
						} else {
							switch (employe.getDivisionCode()) {
								case 0:
									scoreRatio = officer.get(employe.getPosition());
									break;
								case 1:
									scoreRatio = commandTeam.get(employe.getPosition());
									break;
								case 2:
									scoreRatio = tGridTeam.get(employe.getPosition());
									break;
								case 3:
									scoreRatio = degitalTeam.get(employe.getPosition());
									break;
								case 4:
									scoreRatio = distributeTeam.get(employe.getPosition());
									break;
								case 5:
									scoreRatio = developTeam.get(employe.getPosition());
									break;
								case 6:
									scoreRatio = financeTeam.get(employe.getPosition());
									break;
								default:
									break;
							}
						}
						resultProjectScore = ResultProjectScore.builder()
								.seq(projectScore.getSeq())
								.month(projectScore.getMonth())
								.score1(projectScore.getScore1())
								.score2(projectScore.getScore2())
								.score3(projectScore.getScore3())
								.score4(projectScore.getScore4())
								.score5(projectScore.getScore5())
								.score6(projectScore.getScore6())
								.score7(projectScore.getScore7())
								.score8(scoreRatio)
								.status(projectScore.getStatus())
								.dissent(projectScore.getDissent())
								.comments1(projectScore.getComments1())
								.comments2(projectScore.getComments2())
								.confirmNumber(projectScore.getConfirmNumber())
								.confirm1(projectScore.getConfirm1())
								.confirm2(projectScore.getConfirm2())
								.build();
					}
				}
				resultProjectScore.setEmploye(employe.getEmployeId(),
						employe.getEmployeName(),
						employe.getDivisionName(),
						employe.getDivisionCode(),
						employe.getPosition(),
						employe.getPosition() == 3 ? "임원"
								: ((employe.getDivisionCode() > 0 && employe.getTeamCode() == 0) ? "부서장"
										: employe.getTeamName()),
						employe.getTeamCode(),
						employe.getAuthLevel());

				resultList.add(resultProjectScore);
			}
		}
		resultMap.put("resultProjectScore", resultList);
		resultMap.put("scoreHistory", projectScoreHistoryList);

		logger.debug("getProjectScoreList=> {}, EmployeList=> {}", projectScoreList.size(), employeList.size());
		logger.info("getProjectScoreListDivision | OUT |");

		return resultMap;

	}

	public boolean insertProjectScore(ProjectScore projectScore) {
		logger.info("insertProjectScoreList | IN |");
		boolean state = false;
		state = personalEvaluationDao.insertProjectScore(projectScore);
		logger.info("insertProjectScoreList | OUT |");
		return state;
	}

	public boolean deleteProjectScore(List<Integer> seqList) {
		logger.info("deleteProjectScoreList | IN |");

		HashMap<String, Object> seqMap = new HashMap<String, Object>();
		boolean state = false;

		seqMap.put("seqList", seqList);
		state = personalEvaluationDao.deleteProjectScore(seqMap);
		logger.info("deleteProjectScoreList | OUT |");
		return state;
	}

	public boolean updateProjectScore(ProjectScore projectScore) {
		logger.info("updateProjectScoreList | IN |");

		boolean state = false;

		state = personalEvaluationDao.updateProjectScore(projectScore);
		logger.info("updateProjectScoreList | OUT |");
		return state;
	}

	public ResultPerformanceScore getPerformanceScore(String employeId, int authLevel, String yyyyMM) {
		logger.info("getPerformanceScoreList | IN |");

		ResultPerformanceScore performanceScore = personalEvaluationDao.getPerformanceScore(employeId, authLevel,
				yyyyMM);
		logger.debug("getPerformanceScoreList=> {}", performanceScore);
		logger.info("getPerformanceScoreList | OUT |");
		return performanceScore;
	}

	public List<ResultPerformanceScore> getPerformanceScoreList(String employeId, int authLevel, String yyyyMM) {
		logger.info("getPerformanceScoreListAll | IN |");

		List<ResultPerformanceScore> performanceScoreList = personalEvaluationDao.getPerformanceScoreList(employeId,
				authLevel, yyyyMM);
		List<ResultPerformanceScore> performanceScoreResult = new ArrayList<ResultPerformanceScore>();
		Employe employeInfo = getEmploye(employeId);

		logger.debug("Total PerformanceScoreList=> {}, authLevel=> ()", performanceScoreList.size(), authLevel);
		if (employeInfo.getPosition() < 4 && authLevel < 4) {
			for (ResultPerformanceScore eachValue : performanceScoreList) {
				// logger.debug("eachValue=>"+eachValue.getConfirm2());
				if (eachValue.getConfirm1().equals(employeId) || eachValue.getConfirm2().equals(employeId)) {// 1차평가자,
																												// 2차평가자시
																												// 리스트
																												// 추가
					performanceScoreResult.add(eachValue);
				} else if (eachValue.getEmployeId().equals(employeId)) {// 본인 데이터 리스트 추가
					performanceScoreResult.add(eachValue);
				} else if (employeInfo.getPosition() >= 3 && eachValue.getScore1() > 0 && eachValue.getScore2() > 0) {// 임원진에게
																														// 평가완료된
																														// 피평가자
																														// 리스트
																														// 추가
					performanceScoreResult.add(eachValue);
				}
			}
		} else if (authLevel >= 4) {
			performanceScoreResult = performanceScoreList;
		}

		logger.debug("Added PerformanceScoreList=> {}", performanceScoreResult.size());
		logger.info("getPerformanceScoreListAll | OUT |");
		return performanceScoreResult;
	}

	public boolean insertPerformanceScore(PerformanceScore performanceScore) {
		logger.info("insertPerformanceScoreList | IN |");
		boolean state = false;
		state = personalEvaluationDao.insertPerformanceScore(performanceScore);
		logger.info("insertPerformanceScoreList | OUT |");
		return state;
	}

	public boolean deletePerformanceScore(List<Integer> seqList) {
		logger.info("deletePerformanceScoreList | IN |");

		HashMap<String, Object> seqMap = new HashMap<String, Object>();
		boolean state = false;

		seqMap.put("seqList", seqList);
		state = personalEvaluationDao.deletePerformanceScore(seqMap);
		logger.info("deletePerformanceScoreList | OUT |");
		return state;
	}

	public boolean updatePerformanceScore(PerformanceScore performanceScore) {
		logger.info("updatePerformanceScoreList | IN |");

		boolean state = false;
		state = personalEvaluationDao.updatePerformanceScore(performanceScore);

		logger.info("updatePerformanceScoreList | OUT |");
		return state;
	}

	public List<ResultValueScore> getValueScore(String searchId, int authLevel, String yyyyMM) {
		logger.info("getValueScoreList | IN |");

		List<ResultValueScore> valueScoreList = personalEvaluationDao.getValueScore(searchId, authLevel, yyyyMM);
		logger.info("getValueScoreList | OUT |");
		return valueScoreList;
	}

	public List<ResultValueScore> getValueScoreAll(String employeId, int authLevel, String yyyyMM) {
		logger.info("getValueScoreListAll | IN |");

		List<ResultValueScore> valueScoreList = personalEvaluationDao.getValueScoreAll(employeId, authLevel, yyyyMM);
		logger.info("getValueScoreListAll | OUT |");

		return valueScoreList;
	}

	public boolean insertValueScore(ValueScore valueScore) {
		logger.info("insertValueScoreList | IN |");

		boolean state = false;
		state = personalEvaluationDao.insertValueScore(valueScore);

		logger.info("insertValueScoreList | OUT |");

		return state;

	}

	public boolean deleteValueScore(List<Integer> seqList) {
		logger.info("deleteValueScoreList | IN |");

		HashMap<String, Object> seqMap = new HashMap<String, Object>();
		boolean state = false;

		seqMap.put("seqList", seqList);
		state = personalEvaluationDao.deleteValueScore(seqMap);
		logger.info("deleteValueScoreList | OUT |");
		return state;
	}

	public boolean updateValueScore(ValueScore valueceScore) {
		logger.info("updateValueScoreList | IN |");

		boolean state = false;

		state = personalEvaluationDao.updateValueScore(valueceScore);
		logger.info("updateValueScoreList | OUT |");
		return state;
	}

	public boolean saveProjectScore(List<ProjectScore> projectScoreList) {
		logger.info("saveProjectScoreList | IN | projectScoreList.size=> {}", projectScoreList.size());

		boolean state = false;
		int count = 0;
		try {
			for (ProjectScore projectScore : projectScoreList) {
				switch (projectScore.getConfirmNumber()) {
					case 2:
						projectScore.setStatus(3);
						break;
					case 1:
						projectScore.setStatus(2);
						break;
					case 0:
						projectScore.setStatus(1);
						break;
					default:
						projectScore.setStatus(0);
						break;
				}

				// 사장님 예외처리
				if (projectScore.getEmployeId().equals("990101")) {
					projectScore.setStatus(3);
				}

				logger.debug("seq=> {}, status => {}", projectScore.getSeq(), projectScore.getStatus());

				count = personalEvaluationDao.getProjectScoreCount(projectScore.getEmployeId(),
						projectScore.getMonth());
				if (logger.isDebugEnabled())
					logger.debug(String.format("empId=> %s, month => %s, count => %d", projectScore.getEmployeId(),
							projectScore.getMonth(), count));

				if (projectScore.getSeq() > 0) {
					state = personalEvaluationDao.updateProjectScore(projectScore);
					if (projectScore.getStatus() == 1) {
						personalEvaluationDao.updateProjectScoreHistory(projectScore);
					}
				} else {
					state = personalEvaluationDao.insertProjectScore(projectScore);
					if (projectScore.getStatus() == 1) {
						personalEvaluationDao.insertProjectScoreHistory(projectScore);
					}
				}
				if (!state) {
					return state;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("saveProjectScoreList | OUT |");
		return state;
	}

	public boolean saveProjectScoreAdmin(List<ProjectScore> projectScoreList) {
		logger.info("saveProjectScoreList | IN | projectScoreList.size=> {}", projectScoreList.size());

		int count = 0;
		boolean state = false;
		try {
			for (ProjectScore projectScore : projectScoreList) {
				// 사장님 예외처리
				projectScore.setStatus(3);

				logger.debug("state" + projectScore.getStatus() + "saveStatus" + projectScore.getStatus() + "seq=>"
						+ projectScore.getSeq());

				count = personalEvaluationDao.getProjectScoreCount(projectScore.getEmployeId(),
						projectScore.getMonth());

				if (projectScore.getSeq() > 0) {
					state = personalEvaluationDao.updateProjectScore(projectScore);
					if (projectScore.getStatus() == 1) {
						personalEvaluationDao.updateProjectScoreHistory(projectScore);
					}
				} else {
					state = personalEvaluationDao.insertProjectScore(projectScore);
					if (projectScore.getStatus() == 1) {
						personalEvaluationDao.insertProjectScoreHistory(projectScore);
					}
				}
				if (!state) {
					return state;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("saveProjectScoreList | OUT |");
		return state;
	}

	public PerformanceValue getTotalScore(String employeId, int authLevel, String yyyyMM) {
		logger.info("getTotalScoreList | IN |");

		PerformanceValue performanceValue = new PerformanceValue();
		List<ResultValueScore> valueScoreList = getValueScore(employeId, authLevel, yyyyMM);
		ResultPerformanceScore performanceScore = getPerformanceScore(employeId, authLevel, yyyyMM);

		/* 가치평가Data */
		for (ResultValueScore valueScore : valueScoreList) {
			if (performanceScore.getEmployeId().equals(valueScore.getEmployeId())) {
				List<Boolean> ScoreList = new ArrayList<Boolean>();

				ScoreList.add((valueScore.getScore1() > 0) ? true : false);
				ScoreList.add((valueScore.getScore2() > 0) ? true : false);
				ScoreList.add((valueScore.getScore3() > 0) ? true : false);
				ScoreList.add((valueScore.getScore4() > 0) ? true : false);
				ScoreList.add((valueScore.getScore5() > 0) ? true : false);
				ScoreList.add((valueScore.getScore6() > 0) ? true : false);
				ScoreList.add((valueScore.getScore7() > 0) ? true : false);
				ScoreList.add((valueScore.getScore8() > 0) ? true : false);

				if (valueScore.getConfirmNumber() == 1) {
					performanceValue.setValueComments1(valueScore.getComments());
					performanceValue.setValueScore1(ScoreList);
				} else if (valueScore.getConfirmNumber() == 2) {
					performanceValue.setValueComments2(valueScore.getComments());
					performanceValue.setValueScore2(ScoreList);
				}
				logger.debug("valueScore1 = {}", performanceValue.getValueScore1());
				logger.debug("valueScore2 = {}", performanceValue.getValueScore2());
			}
		}

		performanceValue.setEmployeValues(performanceScore.getEmployeId(),
				performanceScore.getEmployeName(),
				performanceScore.getDivisionName(),
				((performanceScore.getDivisionCode() > 0 && performanceScore.getTeamCode() == 0) ? "부서장"
						: performanceScore.getTeamName()),
				performanceScore.getAuthLevel(),
				performanceScore.getConfirm1(),
				performanceScore.getConfirm2());

		performanceValue.setPerfomValues(performanceScore.getScore1(),
				performanceScore.getScore2(),
				yyyyMM);

		performanceValue.setPerformComments1(performanceScore.getComments1());
		performanceValue.setPerformComments2(performanceScore.getComments2());

		performanceValue.setDissentComments1(performanceScore.getDissentComments1());
		performanceValue.setDissentComments2(performanceScore.getDissentComments2());

		performanceValue.setRatio((5 * 0.1), (5 * 0.1));

		logger.info("getTotalScoreList | OUT |");
		return performanceValue;
	}

	public HashMap<String, Object> getTotalScoreMap(String employeId, int authLevel, String yyyyMM) {
		logger.info("getTotalScoreUserData | IN |employeId=" + employeId + " : authLevel=" + authLevel + "yyyyMM="
				+ yyyyMM);

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		ResultPerformanceScore performanceScore = getPerformanceScore(employeId, 0, yyyyMM);
		List<ResultValueScore> valueScoreList = getValueScoreAll(employeId, 0, yyyyMM);

		ResultPerformanceScore performanceScoreHistory = personalEvaluationDao.getPerformanceScoreHistory(employeId, 0,
				yyyyMM);
		List<ResultValueScore> valueScoreHistoryList = personalEvaluationDao.getValueScoreHistoryAll(employeId, 0,
				yyyyMM);

		/* 가치평가Data */
		for (ResultValueScore valueScore : valueScoreList) {
			List<Boolean> ScoreList = new ArrayList<Boolean>();
			if (performanceScore.getEmployeId().equals(valueScore.getEmployeId())) {
				ScoreList.add((valueScore.getScore1() > 0) ? true : false);
				ScoreList.add((valueScore.getScore2() > 0) ? true : false);
				ScoreList.add((valueScore.getScore3() > 0) ? true : false);
				ScoreList.add((valueScore.getScore4() > 0) ? true : false);
				ScoreList.add((valueScore.getScore5() > 0) ? true : false);
				ScoreList.add((valueScore.getScore6() > 0) ? true : false);
				ScoreList.add((valueScore.getScore7() > 0) ? true : false);
				ScoreList.add((valueScore.getScore8() > 0) ? true : false);

				if (valueScore.getConfirmNumber() == 1) {
					resultMap.put("valueScore1", ScoreList);
					resultMap.put("comments1", valueScore.getComments());
				} else if (valueScore.getConfirmNumber() == 2) {
					resultMap.put("valueScore2", ScoreList);
					resultMap.put("comments2", valueScore.getComments());
				}
			}
		}
		logger.debug("valueScoreHistoryList" + valueScoreHistoryList.size());
		for (ResultValueScore valueScoreHistory : valueScoreHistoryList) {

			if (performanceScoreHistory.getEmployeId().equals(valueScoreHistory.getEmployeId())) {
				double score = 0;

				if (valueScoreHistory.getScore1() > 0)
					score++;
				if (valueScoreHistory.getScore2() > 0)
					score++;
				if (valueScoreHistory.getScore3() > 0)
					score++;
				if (valueScoreHistory.getScore4() > 0)
					score++;
				if (valueScoreHistory.getScore5() > 0)
					score++;
				if (valueScoreHistory.getScore6() > 0)
					score++;
				if (valueScoreHistory.getScore7() > 0)
					score++;
				if (valueScoreHistory.getScore8() > 0)
					score++;

				score = (score > 0) ? score + 2 : 0;

				logger.debug("valueScoreHistory.getScore1() = {}", score);
				if (valueScoreHistory.getConfirmNumber() == 1) {
					resultMap.put("performScoreHistory1", (double) performanceScoreHistory.getScore1());
					resultMap.put("valueScoreHistory1", score);
				} else if (valueScoreHistory.getConfirmNumber() == 2) {
					resultMap.put("performScoreHistory2", (double) performanceScoreHistory.getScore2());
					resultMap.put("valueScoreHistory2", score);
				}
			}
		}

		resultMap.put("performanceScore", performanceScore);
		resultMap.put("valueRatio", (5 * 0.1));
		resultMap.put("performRatio", (5 * 0.1));

		logger.info("getTotalScoreUserData | OUT | performanceValueList");
		return resultMap;
	}

	public List<PerformanceValue> getTotalScoreList(String employeId, int authLevel, String yyyyMM) {
		logger.info("getTotalScoreListAll | IN |");

		List<PerformanceValue> performanceValueList = new ArrayList<PerformanceValue>();
		List<ResultValueScore> valueScoreList = getValueScoreAll(employeId, authLevel, yyyyMM);
		List<ResultPerformanceScore> performanceScoreList = getPerformanceScoreList(employeId, authLevel, yyyyMM);

		for (ResultPerformanceScore performanceScore : performanceScoreList) {
			PerformanceValue pV = new PerformanceValue();

			/* 가치평가Data */
			for (ResultValueScore valueScore : valueScoreList) {
				if (performanceScore.getEmployeId().equals(valueScore.getEmployeId())) {
					List<Boolean> ScoreList = new ArrayList<Boolean>();
					ScoreList.add((valueScore.getScore1() > 0) ? true : false);
					ScoreList.add((valueScore.getScore2() > 0) ? true : false);
					ScoreList.add((valueScore.getScore3() > 0) ? true : false);
					ScoreList.add((valueScore.getScore4() > 0) ? true : false);
					ScoreList.add((valueScore.getScore5() > 0) ? true : false);
					ScoreList.add((valueScore.getScore6() > 0) ? true : false);
					ScoreList.add((valueScore.getScore7() > 0) ? true : false);
					ScoreList.add((valueScore.getScore8() > 0) ? true : false);

					if (valueScore.getConfirmNumber() == 1) {
						pV.setValueScore1(ScoreList);
					} else if (valueScore.getConfirmNumber() == 2) {
						pV.setValueScore2(ScoreList);
					}
				}
			}

			/* 가치평가Data */

			pV.setEmployeValues(performanceScore.getEmployeId(),
					performanceScore.getEmployeName(),
					performanceScore.getDivisionName(),
					((performanceScore.getDivisionCode() > 0 && performanceScore.getTeamCode() == 0) ? "부서장"
							: performanceScore.getTeamName()),
					performanceScore.getAuthLevel(),
					performanceScore.getConfirm1(),
					performanceScore.getConfirm2());

			pV.setPerfomValues(performanceScore.getScore1(), performanceScore.getScore2(), yyyyMM);
			pV.setDissent(performanceScore.getDissent());
			pV.setPerformComments1(performanceScore.getComments1());
			pV.setPerformComments2(performanceScore.getComments2());
			pV.setDissentComments1(performanceScore.getDissentComments1());
			pV.setDissentComments2(performanceScore.getDissentComments2());
			pV.setRatio((5 * 0.1), (5 * 0.1));
			performanceValueList.add(pV);
		}

		logger.info("getTotalScoreListAll | OUT |performanceValueList.size=> {}", performanceValueList.size());
		return performanceValueList;
	}

	public boolean saveTotalScore(PerformanceValue performanceValue, int confirmNumber) {
		logger.info("saveTotalScore | IN |");
		boolean isSucceeded = false;
		try {
			PerformanceScore performanceScore = new PerformanceScore();
			ValueScore valueScore = new ValueScore();
			List<Integer> valueInt = new ArrayList<Integer>();

			performanceScore.setPerformanceScore(performanceValue.getMonth(),
					performanceValue.getEmployeId(),
					"", "", confirmNumber);
			if (confirmNumber == 1) {
				for (boolean value : performanceValue.getValueScore1()) {
					if (value) {
						valueInt.add(1);
					} else {
						valueInt.add(0);
					}
				}
				performanceScore.setScore(performanceValue.getPerfomScore1());
				performanceScore.setComments(performanceValue.getPerformComments1());
				performanceScore.setDissentComments(performanceValue.getDissentComments1());
				valueScore.setComments(performanceValue.getValueComments1());
			} else if (confirmNumber == 2) {
				for (boolean value : performanceValue.getValueScore2()) {
					if (value) {
						valueInt.add(1);
					} else {
						valueInt.add(0);
					}
				}

				performanceScore.setScore(performanceValue.getPerfomScore2());
				performanceScore.setComments(performanceValue.getPerformComments2());
				performanceScore.setDissentComments(performanceValue.getDissentComments2());
				valueScore.setComments(performanceValue.getValueComments2());
			}

			valueScore.setEmployeId(performanceValue.getEmployeId());
			valueScore.setMonth(performanceValue.getMonth());
			valueScore.setScore1(valueInt.get(0));
			valueScore.setScore2(valueInt.get(1));
			valueScore.setScore3(valueInt.get(2));
			valueScore.setScore4(valueInt.get(3));
			valueScore.setScore5(valueInt.get(4));
			valueScore.setScore6(valueInt.get(5));
			valueScore.setScore7(valueInt.get(6));
			valueScore.setScore8(valueInt.get(7));
			valueScore.setConfirmNumber(confirmNumber);

			if (personalEvaluationDao.getPerformanceScoreCount(performanceValue.getEmployeId(),
					confirmNumber,
					performanceValue.getMonth()) > 0) {
				isSucceeded = personalEvaluationDao.updatePerformanceScore(performanceScore);
				if (performanceScore.getDissentComments().equals("") && isSucceeded) {
					personalEvaluationDao.updatePerformanceScoreHistory(performanceScore);
				}
			} else {

				logger.debug("performanceScore.getDissentComments() => {} ", performanceScore.getDissentComments());

				isSucceeded = personalEvaluationDao.insertPerformanceScore(performanceScore);
				if (performanceScore.getDissentComments().equals("") && isSucceeded) {
					personalEvaluationDao.insertPerformanceScoreHistory(performanceScore);
				}
			}

			if (personalEvaluationDao.getValueScoreCount(performanceValue.getEmployeId(),
					confirmNumber,
					performanceValue.getMonth()) > 0) {
				isSucceeded = personalEvaluationDao.updateValueScore(valueScore);
				if (performanceScore.getDissentComments().equals("") && isSucceeded) {
					personalEvaluationDao.updateValueScoreHistory(valueScore);
				}
			} else {
				isSucceeded = personalEvaluationDao.insertValueScore(valueScore);
				if (performanceScore.getDissentComments().equals("") && isSucceeded) {
					personalEvaluationDao.insertValueScoreHistory(valueScore);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("saveTotalScore | OUT |");
		return isSucceeded;
	}

	public boolean saveTotalScoreCEO(PerformanceValue performanceValue) {
		logger.info("saveTotalScore | IN |");
		boolean isSucceeded = false;
		try {
			PerformanceScore performanceScore = new PerformanceScore();
			ValueScore valueScore = new ValueScore();
			List<Integer> valueInt = new ArrayList<Integer>();
			for (int confirmNumber = 1; confirmNumber <= 2; confirmNumber++) {
				performanceScore.setPerformanceScore(performanceValue.getMonth(),
						performanceValue.getEmployeId(),
						"", "", confirmNumber);
				if (confirmNumber == 1) {
					for (boolean value : performanceValue.getValueScore1()) {
						if (value) {
							valueInt.add(1);
						} else {
							valueInt.add(0);
						}
					}
					performanceScore.setScore(performanceValue.getPerfomScore1());
					performanceScore.setComments(performanceValue.getPerformComments1());
					performanceScore.setDissentComments(performanceValue.getDissentComments1());
					valueScore.setComments(performanceValue.getValueComments1());
				} else if (confirmNumber == 2) {
					for (boolean value : performanceValue.getValueScore2()) {
						if (value) {
							valueInt.add(1);
						} else {
							valueInt.add(0);
						}
					}

					performanceScore.setScore(performanceValue.getPerfomScore2());
					performanceScore.setComments(performanceValue.getPerformComments2());
					performanceScore.setDissentComments(performanceValue.getDissentComments2());
					valueScore.setComments(performanceValue.getValueComments2());
				}

				valueScore.setEmployeId(performanceValue.getEmployeId());
				valueScore.setMonth(performanceValue.getMonth());
				valueScore.setScore1(valueInt.get(0));
				valueScore.setScore2(valueInt.get(1));
				valueScore.setScore3(valueInt.get(2));
				valueScore.setScore4(valueInt.get(3));
				valueScore.setScore5(valueInt.get(4));
				valueScore.setScore6(valueInt.get(5));
				valueScore.setScore7(valueInt.get(6));
				valueScore.setScore8(valueInt.get(7));
				valueScore.setConfirmNumber(confirmNumber);

				if (personalEvaluationDao.getPerformanceScoreCount(performanceValue.getEmployeId(),
						confirmNumber,
						performanceValue.getMonth()) > 0) {
					isSucceeded = personalEvaluationDao.updatePerformanceScore(performanceScore);
					if (performanceScore.getDissentComments().equals("") && isSucceeded) {
						personalEvaluationDao.updatePerformanceScoreHistory(performanceScore);
					}
				} else {

					logger.debug("performanceScore.getDissentComments() => ", performanceScore.getDissentComments());

					isSucceeded = personalEvaluationDao.insertPerformanceScore(performanceScore);
					if (performanceScore.getDissentComments().equals("") && isSucceeded) {
						personalEvaluationDao.insertPerformanceScoreHistory(performanceScore);
					}
				}

				if (personalEvaluationDao.getValueScoreCount(performanceValue.getEmployeId(),
						confirmNumber,
						performanceValue.getMonth()) > 0) {
					isSucceeded = personalEvaluationDao.updateValueScore(valueScore);
					if (performanceScore.getDissentComments().equals("") && isSucceeded) {
						personalEvaluationDao.updateValueScoreHistory(valueScore);
					}
				} else {
					isSucceeded = personalEvaluationDao.insertValueScore(valueScore);
					if (performanceScore.getDissentComments().equals("") && isSucceeded) {
						personalEvaluationDao.insertValueScoreHistory(valueScore);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		logger.info("saveTotalScore | OUT |");
		return isSucceeded;
	}

	public List<Employe> getEmployeList(int divisionCode, String useYn) {
		logger.info("getEmployeList | IN |");

		List<Employe> result = personalEvaluationDao.getEmployeList(divisionCode, useYn);

		logger.info("getEmployeList | OUT |");

		return result;
	}

	public void setRatio() {

		/* 유통사업본부 */
		distributeTeam.add(0); // 팀원
		distributeTeam.add(0); // 팀장
		distributeTeam.add(0); // 본부장
		distributeTeam.add(0); // 임원

		/* 디지털사업본부 */
		degitalTeam.add(0);
		degitalTeam.add(0);
		degitalTeam.add(0);
		degitalTeam.add(0);

		/* T-GRID사업본부 */
		tGridTeam.add(0);
		tGridTeam.add(0);
		tGridTeam.add(0);
		tGridTeam.add(0);

		/* 기업부설연구소 */
		developTeam.add(0);
		developTeam.add(0);
		developTeam.add(0);
		developTeam.add(0);

		/* 관리본부 */
		commandTeam.add(0);
		commandTeam.add(0);
		commandTeam.add(0);
		commandTeam.add(0);

		/* 재무본부 */
		financeTeam.add(0);
		financeTeam.add(0);
		financeTeam.add(0);
		financeTeam.add(0);

		/* 임원 */
		officer.add(0);
		officer.add(0);
		officer.add(0);
		officer.add(10);
	}

	public boolean saveProjectDissent(ProjectScore projectScore) {
		logger.debug("saveProjectDissent | IN |");

		boolean state = false;

		try {
			state = personalEvaluationDao.updateProjectComments(projectScore);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		logger.debug("saveProjectDissent | OUT |");
		return state;
	}

	public boolean savePerformDissent(PerformanceScore performanceScore) {
		logger.debug("savePerformDissent | IN |");

		boolean state = false;

		try {
			state = personalEvaluationDao.updatePerformanceDissent(performanceScore);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		logger.debug("savePerformDissent | OUT |");
		return state;
	}

	public Employe getEmploye(String employeId) {
		Employe employe = personalEvaluationDao.getEmploye(employeId);
		return employe;
	}

	public boolean updatePassword(String employeId, String password) {
		Employe employe = new Employe();
		employe.setEmployeId(employeId);
		employe.setPassword(password);
		boolean result = personalEvaluationDao.updateEmploye(employe);
		return result;
	}

    public boolean resetEmployePassword(String employeId) {
        logger.info("resetEmployePassword | IN | employeId: {}", employeId);
        boolean result = false;
        try {
            // Define a default password. In a real application, this should be more secure.
            // For this example, assuming DAO/Mapper handles hashing or a plain text default.
            // If the application uses Spring Security's PasswordEncoder, it should be injected and used here.
            final String DEFAULT_PASSWORD = "ktree123!"; 
            
            result = updatePassword(employeId, DEFAULT_PASSWORD); // Using the existing updatePassword method
        } catch (Exception e) {
            logger.error("Error resetting password for employeId: {}", employeId, e);
        }
        logger.info("resetEmployePassword | OUT | result: {}", result);
        return result;
    }

	public List<DivisionInfo> getDivisionList() {
		List<DivisionInfo> result = personalEvaluationDao.getDivisionList();

		return result;
	}

	public List<TeamInfo> getTeamList(int divisionCode) {
		List<TeamInfo> result = personalEvaluationDao.getTeamList(divisionCode);

		return result;
	}

}
