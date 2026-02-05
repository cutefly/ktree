package kr.co.kpcard.ktree.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import jakarta.annotation.Resource;
import kr.co.kpcard.ktree.domain.DivisionInfo;
import kr.co.kpcard.ktree.domain.EmployeInfo;
import kr.co.kpcard.ktree.domain.PerformanceScore;
import kr.co.kpcard.ktree.domain.ProjectScore;
import kr.co.kpcard.ktree.domain.ResultPerformanceScore;
import kr.co.kpcard.ktree.domain.ResultValueScore;
import kr.co.kpcard.ktree.domain.TeamInfo;
import kr.co.kpcard.ktree.domain.ValueScore;
import lombok.RequiredArgsConstructor;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PersonalEvaluationDao {

	private final Logger logger = LoggerFactory.getLogger(PersonalEvaluationDao.class);

	private final SqlSession sqlSession;

	@Resource(name = "transactionManager")
	protected DataSourceTransactionManager transactionManager;

	public EmployeInfo getEmploye(String employeId) {
		logger.info("getEmploye | IN | Param : employeId=> {}", employeId);

		EmployeInfo result = new EmployeInfo();
		HashMap<String, Object> param = new HashMap<String, Object>();
		try {
			param.put("employeId", employeId);
			result = sqlSession.selectOne("Personal.getEmploye", param);
			if (result != null) {
				logger.info("getEmploye | OUT | Param : result=> {}", result.getEmployeName());
			}
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		return result;
	}

	public List<EmployeInfo> getEmployeList() {
		logger.info("getEmploye | IN | NoParam");

		List<EmployeInfo> result = new ArrayList<EmployeInfo>();
		try {
			result = sqlSession.selectList("Personal.getEmploye");
			if (result != null) {
				logger.info("getEmploye | OUT | Param : result=> {}", result.size());
			}
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		return result;
	}

	public List<EmployeInfo> getEmployeList(int divisionCode, String useYn) {
		logger.info("getEmploye | IN | divisionCode: " + divisionCode + ", useYn: " + useYn);

		List<EmployeInfo> result = new ArrayList<EmployeInfo>();
		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("divisionCode", divisionCode);
			param.put("useYn", useYn);
			result = sqlSession.selectList("Personal.getEmploye", param);
			if (result != null) {
				logger.info("getEmploye | OUT | Param : result=> {}", result.size());
			}
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		return result;
	}

	public boolean insertEmploye(EmployeInfo employe) {
		boolean result = false;

		try {
			sqlSession.insert("");

		} catch (Exception e) {
		}

		return result;
	}

	public boolean updateEmploye(EmployeInfo employe) {
		logger.info("updateEmploye | IN | employe=> {}", employe.getEmployeId());

		boolean result = false;
		try {
			int resultValue = sqlSession.update("Personal.updateEmploye", employe);
			if (resultValue >= 1)
				result = true;
			logger.info("getEmploye | OUT | Param : result=> {}", result);

		} catch (Exception e) {
			logger.error("updateEmploye : {}", e.getMessage());
		}
		return result;
	}

	public boolean deleteEmploye(String seq) {
		return false;
	}

	public ProjectScore getProjectScore(String employeId, String yyyyMM) {
		logger.info("getProjectScore | IN | Param : employeId=>" + employeId + ", yyyyMM=>" + yyyyMM);

		ProjectScore result = new ProjectScore();
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("employeId", employeId);
			map.put("month", yyyyMM);
			result = sqlSession.selectOne("Personal.getProjectScore", map);
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}

		logger.info("getProjectScore | OUT | Param : result=> {}", result);

		return result;
	}

	public List<ProjectScore> getProjectScoreAll(String employeId, String yyyyMM) {
		logger.info("getProjectScoreAll | IN | Param : employeId=>" + employeId + ", yyyyMM=>" + yyyyMM);

		List<ProjectScore> result = new ArrayList<ProjectScore>();
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("employeId", employeId);
			map.put("month", yyyyMM);
			map.put("searchOption", "all");

			result = sqlSession.selectList("Personal.getProjectScore", map);
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}

		logger.info("getProjectScoreAll | OUT | Param : result count=> {}", result.size());

		return result;
	}

	public boolean insertProjectScoreList(List<ProjectScore> projectScoreList) {
		logger.info("insertProjectScore | IN |");
		logger.debug("Param : projectScoreList.size()=> {}", projectScoreList.size());

		boolean result = false;

		try {
			for (ProjectScore projectScore : projectScoreList) {
				logger.debug("insertProjectScore() : projectScore.getEmpId {}", projectScore.getEmployeId());
				int resultValue = sqlSession.insert("Personal.insertProjectScore", projectScore);
				if (resultValue >= 1)
					result = true;
			}

		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("insertProjectScore | OUT | Param : result=> {}", result);

		return result;
	}

	public boolean insertProjectScore(ProjectScore projectScore) {
		logger.info("insertProjectScore | IN |");

		boolean result = false;

		try {
			logger.debug("insertProjectScore() : projectScore.getEmpId {}", projectScore.getEmployeId());
			int resultValue = sqlSession.insert("Personal.insertProjectScore", projectScore);
			if (resultValue >= 1)
				result = true;

		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("insertProjectScore | OUT | Param : result=> {}", result);

		return result;
	}

	public boolean updateProjectScore(ProjectScore projectScore) {
		logger.info("updateProjectScoreListDAO | IN | ");
		boolean result = false;

		try {
			int resultValue = sqlSession.update("Personal.updateProjectScore", projectScore);
			if (resultValue >= 1)
				result = true;
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("updateProjectScoreListDAO | OUT | ");

		return result;
	}

	public boolean deleteProjectScore(HashMap<String, Object> seqMap) {
		logger.info("deleteProjectScoreDAO | IN | ");
		boolean result = false;

		try {
			int resultValue = sqlSession.delete("Personal.deleteProjectScore", seqMap);
			if (resultValue >= 1)
				result = true;
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("deleteProjectScoreDAO | OUT | ");
		return result;
	}

	public ResultPerformanceScore getPerformanceScore(String employeId, int authLevel, String yyyyMM) {
		logger.info("getPerformanceScoregetPerformanceScoreByIdDAO | IN | employeId=>" + employeId + " : authLevel=>"
				+ authLevel + " : yyyyMM=>" + yyyyMM);
		ResultPerformanceScore result = new ResultPerformanceScore();
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("employeId", employeId);
			map.put("authLevel", authLevel);
			map.put("month", yyyyMM);
			result = sqlSession.selectOne("Personal.getPerformanceScoreById", map);
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("getPerformanceScoregetPerformanceScoreByIdDAO | OUT |");
		return result;
	}

	public List<ResultPerformanceScore> getPerformanceScoreList(String employeId, int authLevel, String yyyyMM) {
		logger.info("getPerformanceScoreAllDAO | IN | Param : employeId=>" + employeId + " yyyyMM=>" + yyyyMM);
		List<ResultPerformanceScore> result = new ArrayList<ResultPerformanceScore>();

		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("employeId", employeId);
			EmployeInfo employe = sqlSession.selectOne("Personal.getEmploye", param);

			param.put("authLevel", authLevel);
			param.put("divisionCode", employe.getDivisionCode());
			param.put("teamCode", employe.getTeamCode());
			param.put("month", yyyyMM);

			result = sqlSession.selectList("Personal.getPerformanceScore", param);
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("getPerformanceScoreAllDAO | OUT | Param : result count=> {}", result.size());

		return result;
	}

	public boolean insertPerformanceScore(PerformanceScore performanceScore) {
		logger.debug("insertPerformanceScoreDAO | IN |");

		boolean result = false;

		try {
			logger.debug("insertProjectScore() : projectScore.getEmpId {}", performanceScore.getEmployeId());
			int resultValue = sqlSession.insert("Personal.insertPerformanceScore", performanceScore);
			if (resultValue >= 1)
				result = true;

		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.debug("insertPerformanceScoreDAO | OUT | Param : result=> {}", result);

		return result;
	}

	public boolean updatePerformanceScore(PerformanceScore performanceScore) {
		logger.info("updatePerformanceScoreDAO | IN | ");
		boolean result = false;

		try {
			int resultValue = sqlSession.update("Personal.updatePerformanceScore", performanceScore);
			if (resultValue >= 1)
				result = true;
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("updatePerformanceScoreDAO | OUT | ");

		return result;
	}

	public boolean deletePerformanceScore(HashMap<String, Object> seqMap) {
		logger.info("deletePerformanceScoreDAO | IN |");
		boolean result = false;
		try {
			int resultValue = sqlSession.delete("Personal.deletePerformanceScore", seqMap);
			if (resultValue >= 1)
				result = true;
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("deletePerformanceScoreDAO | OUT | Param : result=> {}", result);
		return result;
	}

	public boolean updatePerformanceDissent(PerformanceScore performanceScore) {
		logger.info("updatePerformanceDissentDAO | IN | ");
		boolean result = false;

		try {
			int resultValue = sqlSession.update("Personal.updatePerformanceDissent", performanceScore);
			if (resultValue >= 1)
				result = true;
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("updatePerformanceDissentDAO | OUT | ");

		return result;
	}

	public List<ResultValueScore> getValueScore(String employeId, int authLevel, String yyyyMM) {
		logger.info("getValueScoreByIdDAO | IN | Param : employeId=>" + employeId + " yyyyMM=>" + yyyyMM);
		List<ResultValueScore> result = new ArrayList<ResultValueScore>();

		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("employeId", employeId);
			map.put("confirmNumber", authLevel);
			map.put("month", yyyyMM);

			result = sqlSession.selectList("Personal.getValueScoreById", map);
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("getValueScoreByIdDAO | OUT | Param : result=> {}", result);

		return result;
	}

	public List<ResultValueScore> getValueScoreAll(String employeId, int authLevel, String yyyyMM) {
		logger.info("getValueScoreAllDAO | IN | employeId=>" + employeId + " : authLevel=>" + authLevel + " : yyyyMM=>"
				+ yyyyMM);
		List<ResultValueScore> result = new ArrayList<ResultValueScore>();
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("employeId", employeId);
			map.put("authLevel", authLevel);
			map.put("month", yyyyMM);
			result = sqlSession.selectList("Personal.getValueScoreAll", map);
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("getValueScoreAllDAO | OUT | result.size()=> {}", result.size());
		return result;
	}

	public boolean insertValueScore(ValueScore valueScore) {
		logger.info("insertValueScoreDAO | IN | ");

		boolean result = false;

		try {
			// logger.debug("insertProjectScore() :
			// projectScore.getEmpId"+valueScore.getEmployeId());
			int resultValue = sqlSession.insert("Personal.insertValueScore", valueScore);
			if (resultValue >= 1)
				result = true;

		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("insertValueScoreDAO | OUT | Param : result=> {}", result);

		return result;
	}

	public boolean updateValueScore(ValueScore valueScore) {
		logger.info("updateValueScoreDAO | IN |");
		boolean result = false;

		try {
			int resultValue = sqlSession.delete("Personal.updateValueScore", valueScore);
			if (resultValue >= 1)
				result = true;
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}

		logger.info("updateValueScoreDAO | OUT |");
		return result;

	}

	public boolean deleteValueScore(HashMap<String, Object> seqMap) {
		logger.info("deleteValueScoreDAO | IN |");
		boolean result = false;

		try {
			int resultValue = sqlSession.delete("Personal.deleteValueScore", seqMap);
			if (resultValue >= 1)
				result = true;
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}

		logger.info("deleteValueScoreDAO | OUT |");
		return result;
	}

	public int getPerformanceScoreCount(String employeId, int authLevel, String yyyyMM) {
		logger.info("getPerformanceScoreCountDAO | IN | employeId=>" + employeId + " : authLevel=>" + authLevel
				+ " : yyyyMM=>" + yyyyMM);
		int result = 0;
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("employeId", employeId);
			map.put("confirmNumber", authLevel);
			map.put("month", yyyyMM);
			result = sqlSession.selectOne("Personal.getPerformanceScoreCount", map);
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("getPerformanceScoreCountDAO | OUT | ");
		return result;
	}

	public int getValueScoreCount(String employeId, int authLevel, String yyyyMM) {
		logger.info("getValueeScoreCountDAO | IN | employeId=>" + employeId + " : authLevel=>" + authLevel
				+ " : yyyyMM=>" + yyyyMM);
		int result = 0;
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("employeId", employeId);
			map.put("confirmNumber", authLevel);
			map.put("month", yyyyMM);
			result = sqlSession.selectOne("Personal.getValueScoreCount", map);
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("getValueeScoreCountDAO | OUT | ");
		return result;
	}

	public ProjectScore getProjectScoreHistory(String employeId, String yyyyMM) {
		logger.info("getProjectScore | IN | Param : employeId=>" + employeId + "yyyyMM=>" + yyyyMM);

		ProjectScore result = new ProjectScore();
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("employeId", employeId);
			map.put("month", yyyyMM);
			result = sqlSession.selectOne("Personal.getProjectScoreHistory", map);
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}

		logger.info("getProjectScore | OUT | Param : result=> {}", result);

		return result;
	}

	public List<ProjectScore> getProjectScoreAllHistory(String employeId, String yyyyMM) {
		logger.info("getProjectScoreAll | IN | Param : employeId=>" + employeId + "yyyyMM=>" + yyyyMM);

		List<ProjectScore> result = new ArrayList<ProjectScore>();
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("employeId", employeId);
			map.put("month", yyyyMM);
			map.put("searchOption", "all");

			result = sqlSession.selectList("Personal.getProjectScoreHistory", map);
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}

		logger.info("getProjectScoreAll | OUT | Param : result count=> {}", result.size());

		return result;
	}

	public boolean insertProjectScoreHistory(ProjectScore projectScore) {
		logger.info("insertProjectScore | IN |");

		boolean result = false;

		try {
			logger.debug("insertProjectScore() : projectScore.getEmpId {}", projectScore.getEmployeId());
			int resultValue = sqlSession.insert("Personal.insertProjectScoreHistory", projectScore);
			if (resultValue >= 1)
				result = true;

		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("insertProjectScore | OUT | Param : result=> {}", result);

		return result;
	}

	public boolean updateProjectScoreHistory(ProjectScore projectScore) {
		logger.info("updateProjectScoreListHistoryDAO | IN | ");
		boolean result = false;

		try {
			int resultValue = sqlSession.update("Personal.updateProjectScoreHistory", projectScore);
			if (resultValue >= 1)
				result = true;
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("updateProjectScoreListHistoryDAO | OUT | ");

		return result;
	}

	public boolean updateProjectComments(ProjectScore projectScore) {
		logger.info("updateProjectScoreListDAO | IN | ");
		boolean result = false;

		try {
			int resultValue = sqlSession.update("Personal.updateProjectScoreComments", projectScore);
			if (resultValue >= 1)
				result = true;
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("updateProjectScoreListDAO | OUT | ");

		return result;
	}

	public boolean insertValueScoreHistory(ValueScore valueScore) {
		logger.info("insertValueScoreHistoryDAO | IN | ");

		boolean result = false;

		try {
			// logger.debug("insertProjectScore() :
			// projectScore.getEmpId"+valueScore.getEmployeId());
			int resultValue = sqlSession.insert("Personal.insertValueScoreHistory", valueScore);
			if (resultValue >= 1)
				result = true;

		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("insertValueScoreHistoryDAO | OUT | Param : result=> {}", result);

		return result;
	}

	public boolean updateValueScoreHistory(ValueScore valueScore) {
		logger.info("updateValueScoreHistoryDAO | IN |");
		boolean result = false;

		try {
			int resultValue = sqlSession.delete("Personal.updateValueScoreHistory", valueScore);
			if (resultValue >= 1)
				result = true;
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}

		logger.info("updateValueScoreHistoryDAO | OUT |");
		return result;

	}

	public boolean insertPerformanceScoreHistory(PerformanceScore performanceScore) {
		logger.debug("insertPerformanceScoreHistoryDAO | IN |");

		boolean result = false;

		try {
			int resultValue = sqlSession.insert("Personal.insertPerformanceScoreHistory", performanceScore);
			if (resultValue >= 1)
				result = true;

		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.debug("insertPerformanceScoreHistoryDAO | OUT | Param : result=> {}", result);

		return result;
	}

	public boolean updatePerformanceScoreHistory(PerformanceScore performanceScore) {
		logger.info("updatePerformanceScoreHistoryDAO | IN | ");
		boolean result = false;

		try {
			int resultValue = sqlSession.update("Personal.updatePerformanceScoreHistory", performanceScore);
			if (resultValue >= 1)
				result = true;
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("updatePerformanceScoreHistoryDAO | OUT | ");

		return result;
	}

	public ResultPerformanceScore getPerformanceScoreHistory(String employeId, int authLevel, String yyyyMM) {
		logger.info("getPerformanceScoregetPerformanceScoreByIdDAO | IN | employeId=>" + employeId + " : authLevel=>"
				+ authLevel + " : yyyyMM=>" + yyyyMM);
		ResultPerformanceScore result = new ResultPerformanceScore();
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("employeId", employeId);
			map.put("authLevel", authLevel);
			map.put("month", yyyyMM);
			result = sqlSession.selectOne("Personal.getPerformanceScoreByIdHistory", map);
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("getPerformanceScoregetPerformanceScoreByIdDAO | OUT |");
		return result;
	}

	public List<ResultPerformanceScore> getPerformanceScoreHistoryList(String employeId, int authLevel, String yyyyMM) {
		logger.info("getPerformanceScoreAllDAO | IN | Param : employeId=>" + employeId + " yyyyMM=>" + yyyyMM);
		List<ResultPerformanceScore> result = new ArrayList<ResultPerformanceScore>();

		try {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("employeId", employeId);
			EmployeInfo employe = sqlSession.selectOne("Personal.getEmploye", param);

			param.put("authLevel", authLevel);
			param.put("divisionCode", employe.getDivisionCode());
			param.put("teamCode", employe.getTeamCode());
			param.put("month", yyyyMM);

			result = sqlSession.selectList("Personal.getPerformanceScoreHistory", param);
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("getPerformanceScoreAllDAO | OUT | Param : result count=> {}", result.size());

		return result;
	}

	public List<ResultValueScore> getValueScoreHistory(String employeId, int authLevel, String yyyyMM) {
		logger.info("getValueScoreByIdDAO | IN | Param : employeId=>" + employeId + " yyyyMM=>" + yyyyMM);
		List<ResultValueScore> result = new ArrayList<ResultValueScore>();

		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("employeId", employeId);
			map.put("confirmNumber", authLevel);
			map.put("month", yyyyMM);

			result = sqlSession.selectList("Personal.getValueScoreByIdHistory", map);
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("getValueScoreByIdDAO | OUT | Param : result=> {}", result);

		return result;
	}

	public List<ResultValueScore> getValueScoreHistoryAll(String employeId, int authLevel, String yyyyMM) {
		logger.info("getValueScoreAllDAO | IN | employeId=>" + employeId + " : authLevel=>" + authLevel + " : yyyyMM=>"
				+ yyyyMM);
		List<ResultValueScore> result = new ArrayList<ResultValueScore>();
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("employeId", employeId);
			map.put("authLevel", authLevel);
			map.put("month", yyyyMM);
			result = sqlSession.selectList("Personal.getValueScoreHistoryAll", map);
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("getValueScoreAllDAO | OUT | result.size()=> {}", result.size());
		return result;
	}

	public int getProjectScoreCount(String employeId, String yyyyMM) {
		logger.info("getValueScoreAllDAO | IN | employeId=>" + employeId + " :  yyyyMM=>" + yyyyMM);
		int result = 0;
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("employeId", employeId);
			map.put("month", yyyyMM);
			result = sqlSession.selectOne("Personal.getProjectScoreCount", map);
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		logger.info("getValueScoreAllDAO | OUT | result=> {}", result);
		return result;
	}

	public List<DivisionInfo> getDivisionList() {
		logger.info("getDivisionList | IN | NoParam");

		List<DivisionInfo> result = new ArrayList<DivisionInfo>();
		try {
			result = sqlSession.selectList("Personal.getDivision");
			if (result != null) {
				logger.info("getDivisionList | OUT | Param : result=> {}", result.size());
			}
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		return result;
	}

	public List<TeamInfo> getTeamList(int divisionCode) {
		logger.info("getTeamList | IN | NoParam");

		List<TeamInfo> result = new ArrayList<TeamInfo>();
		try {
			result = sqlSession.selectList("Personal.getTeam");
			if (result != null) {
				logger.info("getTeamList | OUT | Param : result=> {}", result.size());
			}
		} catch (Exception e) {
			logger.error("exception : {}", e.getMessage());
		}
		return result;
	}

}
