package kr.co.kpcard.ktree.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import kr.co.kpcard.ktree.app.GlobalException;
import kr.co.kpcard.ktree.domain.DivisionInfo;
import kr.co.kpcard.ktree.domain.Employe;
import kr.co.kpcard.ktree.domain.PerformanceScore;
import kr.co.kpcard.ktree.domain.PerformanceValue;
import kr.co.kpcard.ktree.domain.ProjectScore;
import kr.co.kpcard.ktree.domain.ResultPerformanceScore;
import kr.co.kpcard.ktree.domain.ResultProjectScore;
import kr.co.kpcard.ktree.domain.TeamInfo;
import kr.co.kpcard.ktree.service.PersonalEvaluationService;
import kr.co.kpcard.ktree.utility.DateUtil;
import kr.co.kpcard.ktree.utility.ProjectUtil;
import lombok.RequiredArgsConstructor;

//@Controller("PersonalEvaluation")
@Controller
@RequiredArgsConstructor
@RequestMapping("/eval")
@SessionAttributes("Member")
public class PersonalEvaluationController {
	protected final Logger logger = LoggerFactory.getLogger(PersonalEvaluationController.class);

	private final PersonalEvaluationService personalEvaluationService;

	// @Value("${valid.work.date}")
	private Integer validWorkDate = 15; // 영업일

	/**
	 * 프로젝트 기여도 설정 화면
	 * 
	 * @param yyyyMM
	 * @param request
	 * @return 프로젝트 기여도 화면 View
	 * @attributes 조회년월 : yyyyMM, 상신가능여부 : isAvailable
	 */
	@RequestMapping(value = "/projectList")
	public String projectList(@RequestParam(value = "yyyyMM", defaultValue = "") String yyyyMM,
			Model model, jakarta.servlet.http.HttpSession session) {
		String yyyyMMDefault = StringUtils.defaultIfBlank(yyyyMM,
				new SimpleDateFormat("yyyyMM").format(DateUtils.addMonths(new Date(), -1)));

		logger.debug("request performanceList : {}", yyyyMMDefault);
		model.addAttribute("yyyyMM", yyyyMMDefault);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		// For Test
		/*
		 * int testMonth = 2;
		 * int testDay = 11;
		 * cal.set(2021, testMonth-1, testDay);
		 */

		Date today = cal.getTime();
		logger.debug("Today : {}", sdf.format(today));

		Date limitDayOfMonth = getLimitDate(today, validWorkDate);
		model.addAttribute("isAvailable", today.after(limitDayOfMonth) ? Boolean.FALSE : Boolean.TRUE); // 평가 가능한 일자인지
																										// 비교
		logger.info("currentDate : " + sdf.format(today) + ", limitDate : " + sdf.format(limitDayOfMonth)
				+ ", isAvaliable : " + model.getAttribute("isAvailable"));

		List<DivisionInfo> divisionList = personalEvaluationService.getDivisionList();
		logger.debug("divisionList : {}", divisionList.toString());
		List<TeamInfo> teamList = personalEvaluationService.getTeamList(1);
		logger.debug("teamList : {}", teamList.toString());
		model.addAttribute("divisionList", divisionList); // 부서리스트
		model.addAttribute("teamList", teamList); // 팀리스트
		model.addAttribute("sessionAuthLevel", session.getAttribute("authLevel")); // 세션에서 authLevel 추가

		return "sub/project/projectList";
	}

	/**
	 * 프로젝트 기여도 설정 화면(Admin)
	 * 
	 * @param yyyyMM
	 * @param request
	 * @return 프로젝트 기여도 화면 View
	 * @attributes 조회년월 : yyyyMM
	 */
	@RequestMapping(value = "/projectListAdmin")
	public String projectListAdmin(@RequestParam(value = "yyyyMM", defaultValue = "") String yyyyMM,
			Model model, jakarta.servlet.http.HttpSession session) {
		String yyyyMMDefault = StringUtils.defaultIfBlank(yyyyMM,
				new SimpleDateFormat("yyyyMM").format(DateUtils.addMonths(new Date(), -1)));

		logger.debug("request performanceList : {}", yyyyMMDefault);
		model.addAttribute("yyyyMM", yyyyMMDefault);

		// Admin이 아닌 경우 접근 불가
		if (session.getAttribute("employeId").equals("kpc_admin")) {
			List<DivisionInfo> divisionList = personalEvaluationService.getDivisionList();
			List<TeamInfo> teamList = personalEvaluationService.getTeamList(1);
			model.addAttribute("divisionList", divisionList); // 부서리스트
			model.addAttribute("teamList", teamList); // 팀리스트

			return "/sub/project/projectListAdmin";
		} else {
			return "redirect:/";
		}
	}

	/**
	 * 프로젝트 기여도 데이터 리스트
	 * 
	 * @param yyyyMM
	 * @param searchOption : all(전체), 혹은 본인만
	 * @param divisionCode : 특정부서
	 * @param request
	 * @param model
	 * @return view URL
	 * @attribute projectScoreList 프로젝트별 수치
	 * @attribute scoreHistoryList 이력목록
	 */
	@RequestMapping(value = "/projectData")
	public String getProjectData(@RequestParam String yyyyMM,
			@RequestParam String searchOption,
			@RequestParam(value = "divisionCode", defaultValue = "0") int divisionCode,
			Model model, jakarta.servlet.http.HttpSession session) {
		logger.debug("getProjectData | IN | param['yyyyMM':" + yyyyMM + ", 'searchOption':" + searchOption
				+ ", 'divisionCode':" + divisionCode + "]");
		HashMap<String, Object> result = new HashMap<String, Object>();

		try {
			String employeId = (String) session.getAttribute("employeId");
			logger.debug("employeId from session : {}", employeId);
			logger.debug("current : {}, param : {}",
					Integer.parseInt(DateUtil.getCurrentDate("yyyyMM")), Integer.parseInt(yyyyMM));
			if (Integer.parseInt(DateUtil.getCurrentDate("yyyyMM")) > Integer.parseInt(yyyyMM)) {
				if (searchOption.equals("all")) {
					if (divisionCode > 0) {
						result = personalEvaluationService.getProjectScoreDivision(
								employeId, yyyyMM, divisionCode);
					} else {
						result = personalEvaluationService
								.getProjectScoreAll(employeId, yyyyMM);
					}
				} else {
					result = personalEvaluationService
							.getProjectScore(employeId, yyyyMM);
				}

				List<ResultProjectScore> projectScoreList = new ArrayList<>();
				List<ProjectScore> scoreHistList = (List<ProjectScore>) result.get("scoreHistory");
				logger.debug("scoreHistList count : {}", scoreHistList.size());

				for (Object obj : (List<?>) result.get("resultProjectScore")) {
					ResultProjectScore resultProjectScore = (ResultProjectScore) obj;
					logger.debug("resultProjectScore employeId : {}", resultProjectScore.getEmployeId());
					resultProjectScore.setUserAuth(ProjectUtil.calcUseAuth(employeId, resultProjectScore));
					resultProjectScore.setUserDissent(ProjectUtil.calcUseDissent(employeId, resultProjectScore));
					resultProjectScore.setAuthLevel(ProjectUtil.calcAuthLevel(employeId, resultProjectScore));
					resultProjectScore.setEvaluable(ProjectUtil.calcEvaluable(employeId, resultProjectScore));
					resultProjectScore.setDissentable(ProjectUtil.calcDissentable(employeId, resultProjectScore));

					resultProjectScore.setMyScores(ProjectUtil.calcMyScores(employeId, scoreHistList));
					logger.debug("resultProjectScore evaluable : {}", resultProjectScore.isEvaluable());
					projectScoreList.add(resultProjectScore);
				}
				logger.debug("projectScoreList : {}", projectScoreList);
				model.addAttribute("projectScoreList", projectScoreList);

				// logger.debug("scoreHistoryList : {}", result.get("scoreHistory"));
				// model.addAttribute("scoreHistoryList", result.get("scoreHistory"));

				Date today = Calendar.getInstance().getTime();

				Date limitDayOfMonth = getLimitDate(today, validWorkDate);
				model.addAttribute("isAvailable", today.after(limitDayOfMonth) ? Boolean.FALSE : Boolean.TRUE); // 평가가능한
																												// 일자인지
																												// 비교
				model.addAttribute("sessionEmployeId", employeId);
			} else {
				throw new GlobalException("Failed", "현재 월 이후의 데이터는 조회가 불가합니다.");
			}
		} catch (GlobalException e) {
			throw e;
		}
		logger.debug("getProjectData | OUT |");
		return "sub/project/projectData";
	}

	/**
	 * 프로젝트 기여도 데이터 리스트(Admin)
	 * 
	 * @param yyyyMM
	 * @param searchOption : all(전체), 혹은 본인만
	 * @param divisionCode : 특정부서
	 * @param request
	 * @param model
	 * @return
	 * @attribute projectScoreList 프로젝트별 수치
	 * @attribute scoreHistoryList 이력목록
	 */
	@RequestMapping(value = "/projectDataAdmin")
	public String getProjectDataAdmin(@RequestParam String yyyyMM,
			@RequestParam String searchOption,
			@RequestParam(value = "divisionCode", defaultValue = "0") int divisionCode,
			Model model, jakarta.servlet.http.HttpSession session) {
		logger.debug("getProjectData | IN |");
		HashMap<String, Object> result = new HashMap<String, Object>();
		try {
			String employeId = (String) session.getAttribute("employeId");
			if (Integer.parseInt(DateUtil.getCurrentDate("yyyyMM")) > Integer.parseInt(yyyyMM)) {
				if (searchOption.equals("all")) {
					if (divisionCode > 0) {
						result = personalEvaluationService.getProjectScoreDivision(
								employeId, yyyyMM, divisionCode);
					} else {
						result = personalEvaluationService
								.getProjectScoreAll((String) employeId, yyyyMM);
					}
				} else {
					result = personalEvaluationService
							.getProjectScore((String) employeId, yyyyMM);
				}
				List<ResultProjectScore> projectScoreList = new ArrayList<>();
				List<ProjectScore> scoreHistList = (List<ProjectScore>) result.get("scoreHistory");
				logger.debug("scoreHistList count : {}", scoreHistList.size());

				for (Object obj : (List<?>) result.get("resultProjectScore")) {
					ResultProjectScore resultProjectScore = (ResultProjectScore) obj;
					logger.debug("resultProjectScore employeId : {}", resultProjectScore.getEmployeId());
					resultProjectScore.setUserAuth(ProjectUtil.calcUseAuth(employeId, resultProjectScore));
					resultProjectScore.setUserDissent(ProjectUtil.calcUseDissent(employeId, resultProjectScore));
					resultProjectScore.setAuthLevel(ProjectUtil.calcAuthLevel(employeId, resultProjectScore));
					resultProjectScore.setEvaluable(ProjectUtil.calcEvaluable(employeId, resultProjectScore));
					resultProjectScore.setDissentable(ProjectUtil.calcDissentable(employeId, resultProjectScore));

					resultProjectScore.setMyScores(ProjectUtil.calcMyScores(employeId, scoreHistList));
					logger.debug("resultProjectScore evaluable : {}", resultProjectScore.isEvaluable());
					projectScoreList.add(resultProjectScore);
				}
				model.addAttribute("projectScoreList", projectScoreList);
				// model.addAttribute("scoreHistoryList", result.get("scoreHistory"));
			} else {
				throw new GlobalException("Failed", "현재 월 이후의 데이터는 조회가 불가합니다.");
			}
		} catch (GlobalException e) {
			throw e;
		}

		logger.debug("getProjectData | OUT |");

		return "/sub/project/projectDataAdmin";
	}

	/**
	 * 프로젝트 데이터 저장
	 * 
	 * @param seq
	 * @param employeId
	 * @param score1
	 * @param score2
	 * @param score3
	 * @param score4
	 * @param score5
	 * @param score6
	 * @param score7
	 * @param score8
	 * @param yyyyMM
	 * @param confirmNumber : 프로젝트 기여도 확인 스텝 : 0 => 본인설정, 1 => 1차평가자 컨펌, 2 => 2차 평가자
	 *                      컨펌
	 * @param request
	 * @param model
	 * @return S 성공, null 실패
	 */
	@RequestMapping(value = "/saveProjectScore", produces = "application/text; charset=euc-kr")
	@ResponseBody
	public String saveProjectScore(
			@RequestParam("seq[]") List<Integer> seq,
			@RequestParam("employeId[]") List<String> employeId,
			@RequestParam("score1[]") List<Float> score1,
			@RequestParam("score2[]") List<Float> score2,
			@RequestParam("score3[]") List<Float> score3,
			@RequestParam("score4[]") List<Float> score4,
			@RequestParam("score5[]") List<Float> score5,
			@RequestParam("score6[]") List<Float> score6,
			@RequestParam("score7[]") List<Float> score7,
			@RequestParam("score8[]") List<Float> score8,
			@RequestParam("month") String yyyyMM,
			@RequestParam("confirmNumber[]") List<Integer> confirmNumber,
			HttpServletRequest request,
			Model model) {
		logger.debug("save project score");
		List<ProjectScore> projectScoreList = new ArrayList<ProjectScore>();
		String res;
		boolean isSuccessed = false;
		try {
			for (int i = 0; i < employeId.size(); i++) {
				ProjectScore projectScore = ProjectScore.builder()
						.seq(seq.get(i))
						.month(yyyyMM)
						.employeId(employeId.get(i))
						.score1(score1.get(i))
						.score2(score2.get(i))
						.score3(score3.get(i))
						.score4(score4.get(i))
						.score5(score5.get(i))
						.score6(score6.get(i))
						.score7(score7.get(i))
						.score8(score8.get(i))
						.dissent("")
						.comments1("")
						.comments2("")
						.status(0)
						.confirmNumber(confirmNumber.get(i))
						.build();

				logger.debug(
						String.format("add score : seq = %d, value = [%f, %f, %f, %f, %f, %f, %f, %f], confirm = %d",
								seq.get(i),
								score1.get(i), score2.get(i), score3.get(i), score4.get(i), score5.get(i),
								score6.get(i), score7.get(i), score8.get(i),
								confirmNumber.get(i)));
				projectScoreList.add(projectScore);
			}

			if (request.getSession().getAttribute("employeId").equals("kpc_admin")) {
				isSuccessed = personalEvaluationService.saveProjectScoreAdmin(projectScoreList);
			} else {
				isSuccessed = personalEvaluationService.saveProjectScore(projectScoreList);
			}

			if (isSuccessed) {
				res = "S";
			} else {
				throw new GlobalException("Failed", "등록에 실패하였습니다.");
			}
		} catch (GlobalException e) {
			throw e;
		}

		return res;
	}

	/**
	 * 이의신청 팝업 조회
	 * 
	 * @param employeId : 신청자아이디
	 * @param yyyyMM
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/popProjectDissent")
	public String projectDissentPop(@RequestParam(value = "employeId") String employeId,
			@RequestParam(value = "yyyyMM") String yyyyMM, Model model, jakarta.servlet.http.HttpSession session) {
		logger.info("employeId : {}, yyyyMM : {}", employeId, yyyyMM);
		String sessionEmployeId = (String) session.getAttribute("employeId");

		HashMap<String, Object> result = new HashMap<String, Object>();
		result = personalEvaluationService.getProjectScore(employeId, yyyyMM);
		for (ResultProjectScore projectScore : (List<ResultProjectScore>) result.get("resultProjectScore")) {
			model.addAttribute("employeId", employeId);
			model.addAttribute("yyyyMM", yyyyMM);
			model.addAttribute("status", projectScore.getStatus());
			model.addAttribute("dissent", projectScore.getDissent());
			model.addAttribute("comments1", projectScore.getComments1());
			model.addAttribute("comments2", projectScore.getComments2());
			model.addAttribute("authLevel", projectScore.getEmployeLevel());
			model.addAttribute("confirm1", projectScore.getConfirm1());
			model.addAttribute("confirm2", projectScore.getConfirm2());

			// additional attributes for popup
			Date limitDayOfMonth = getLimitDate(Calendar.getInstance().getTime(), validWorkDate);

			int useAuthLevel = ProjectUtil.calcAuthLevel(sessionEmployeId, projectScore);
			Boolean isAvailable = Calendar.getInstance().getTime().after(limitDayOfMonth) ? Boolean.FALSE
					: Boolean.TRUE;

			boolean commentable = false;
			if (employeId.equals(sessionEmployeId) && projectScore.getStatus() < 5)
				commentable = true;
			else if (useAuthLevel == 1 && projectScore.getStatus() < 5)
				commentable = true;
			else if (useAuthLevel == 2 && projectScore.getStatus() < 6)
				commentable = true;

			model.addAttribute("useAuthLevel", useAuthLevel);
			model.addAttribute("isAvailable", isAvailable); // 평가 가능한지
			model.addAttribute("commentable", commentable && isAvailable); // 평가 가능한지
			logger.info("useAuthLevel : {}", model.getAttribute("useAuthLevel"));
			logger.info("isAvailable : {}", model.getAttribute("isAvailable"));
			logger.info("commentable : {}", model.getAttribute("commentable"));
		}
		return "sub/project/projectDissentPop";
	}

	/**
	 * 이의신청 등록
	 * 
	 * @param employeId
	 * @param yyyyMM
	 * @param dissent
	 * @param authLevel
	 * @param comments1
	 * @param comments2
	 * @param request
	 * @return S : 성공, F : 실패
	 */
	@RequestMapping(value = "/saveProjectDissent")
	@ResponseBody
	public String projectDissentPop(@RequestParam(value = "employeId") String employeId,
			@RequestParam(value = "yyyyMM") String yyyyMM,
			@RequestParam(value = "dissent") String dissent,
			@RequestParam(value = "authLevel") int authLevel,
			@RequestParam(value = "comments1", defaultValue = "") String comments1,
			@RequestParam(value = "comments2", defaultValue = "") String comments2,
			HttpServletRequest request) {

		ProjectScore projectScore = new ProjectScore();
		String res;
		int status = 0;
		logger.info("saveProjectDissent | IN | authLevel=>" + authLevel);
		switch (authLevel) {
			case 0:
				status = 4;
				break;
			case 1:
				status = 5;
				break;
			case 2:
				status = 6;
				break;
		}

		projectScore.setEmployeId(employeId);
		projectScore.setMonth(yyyyMM);
		projectScore.setDissent(dissent);
		projectScore.setComments1(comments1);
		projectScore.setComments2(comments2);
		projectScore.setStatus(status);
		projectScore.setConfirmNumber(1);

		if (personalEvaluationService.saveProjectDissent(projectScore)) {
			res = "S";
		} else {
			res = "F";
		}
		return res;
	}

	/**
	 * 직원 관리 화면(Admin)
	 * 
	 * @param yyyyMM
	 * @param request
	 * @return 직원 관리 화면 View
	 * @attributes 조회년월 : yyyyMM
	 */
	@RequestMapping(value = "/employeListAdmin")
	public String employeListAdmin(Model model, jakarta.servlet.http.HttpSession session) {
		logger.debug("request employeList");

		// Admin이 아닌 경우 접근 불가
		if (session.getAttribute("employeId").equals("kpc_admin")) {
			List<DivisionInfo> divisionList = personalEvaluationService.getDivisionList();
			List<TeamInfo> teamList = personalEvaluationService.getTeamList(1);
			model.addAttribute("divisionList", divisionList); // 부서리스트
			model.addAttribute("teamList", teamList); // 팀리스트

			return "/sub/employe/employeListAdmin";
		} else {
			return "redirect:/";
		}
	}

	/**
	 * 직원 데이터 리스트(Admin)
	 * 
	 * @param yyyyMM
	 * @param searchOption : all(전체), 혹은 본인만
	 * @param divisionCode : 특정부서
	 * @param request
	 * @param model
	 * @return
	 * @attribute employeList 직원 리스트
	 */
	@RequestMapping(value = "/employeDataAdmin")
	public String getEmployeDataAdmin(@RequestParam(value = "divisionCode", defaultValue = "0") int divisionCode,
			@RequestParam(value = "useYn", defaultValue = "") String useYn,
			Model model, jakarta.servlet.http.HttpSession session) {
		logger.info("getEmployeData | IN | divisionCode : {}, useYn : {}", divisionCode, useYn);
		try {
			if (session.getAttribute("employeId").equals("kpc_admin")) {
				List<Employe> employeList = personalEvaluationService.getEmployeList(divisionCode, useYn);
				model.addAttribute("employeList", employeList);
			}
		} catch (GlobalException e) {
			throw e;
		}

		logger.debug("getEmployeData | OUT |");

		return "/sub/employe/employeDataAdmin";
	}

    /**
     * 직원 비밀번호 초기화
     * @param employeId
     * @param session
     * @return S: 성공, F: 실패
     */
    @RequestMapping(value = "/resetEmployePassword", produces = "application/text; charset=euc-kr")
    @ResponseBody
    public String resetEmployePassword(@RequestParam("employeId") String employeId, jakarta.servlet.http.HttpSession session) {
        logger.info("resetEmployePassword | IN | employeId : {}", employeId);
        String res = "F";
        try {
            if (session.getAttribute("employeId").equals("kpc_admin")) {
                boolean result = personalEvaluationService.resetEmployePassword(employeId);
                if (result) {
                    res = "S";
                }
            } else {
                logger.warn("Unauthorized attempt to reset password for employeId: {}", employeId);
            }
        } catch (Exception e) {
            logger.error("Error resetting password for employeId: {}", employeId, e);
        }
        logger.info("resetEmployePassword | OUT | result : {}", res);
        return res;
    }

	/**
	 * 성과/가치평가 리스트 화면
	 * 
	 * @param yyyyMM  조회년월(yyyyMM 형식)
	 * @param request
	 * @return 성과/가치평가 조회 화면 View
	 * @attribute yyyyMM : 조회년월
	 */
	@RequestMapping(value = "/performanceList")
	public String performanceList(@RequestParam(value = "yyyyMM", defaultValue = "") String yyyyMM,
			HttpServletRequest request) {
		String yyyyMMDefault = StringUtils.defaultIfBlank(yyyyMM,
				new SimpleDateFormat("yyyyMM").format(DateUtils.addMonths(new Date(), -1)));

		logger.debug("request performanceList : {}", yyyyMMDefault);
		request.setAttribute("yyyyMM", yyyyMMDefault);

		// return "/sub/performance/performanceList";
		// 인사평가 정책 변경에 따라 성과/가치 평가 사용 제한(2021.01.29)
		return "redirect:/";
	}

	/**
	 * 성과/가치평가 리스트 화면(Admin)
	 * 
	 * @param yyyyMM
	 * @param request
	 * @return
	 * @attribute yyyyMM : 조회년월
	 */
	@RequestMapping(value = "/performanceListAdmin")
	public String performanceAdmin(@RequestParam(value = "yyyyMM", defaultValue = "") String yyyyMM,
			HttpServletRequest request) {
		String yyyyMMDefault = StringUtils.defaultIfBlank(yyyyMM,
				new SimpleDateFormat("yyyyMM").format(DateUtils.addMonths(new Date(), -1)));

		logger.debug("request performanceList : {}", yyyyMMDefault);
		request.setAttribute("yyyyMM", yyyyMMDefault);

		// Admin이 아닌 경우 접근 불가
		if (request.getSession().getAttribute("employeId").equals("kpc_admin")) {
			return "/sub/performance/performanceListAdmin";
		} else {
			return "redirect:/";
		}
	}

	/**
	 * 성과/가치평가 조회 데이터 리스트
	 * 
	 * @param yyyyMM
	 * @param searchOption
	 * @param request
	 * @param model
	 * @return 성과/가치평가 조회 데이터 리스트 View
	 * @attribute performanceValueList : 평가리스트
	 */
	@RequestMapping(value = "/performanceListData")
	public String performanceListData(@RequestParam String yyyyMM,
			@RequestParam String searchOption,
			HttpServletRequest request,
			Model model) {

		logger.debug("performanceListData searchSDate=>" + yyyyMM + "session=>"
				+ request.getSession().getAttribute("authLevel"));

		try {
			if (Integer.parseInt(DateUtil.getCurrentDate("yyyyMM")) > Integer.parseInt(yyyyMM)) {
				List<PerformanceValue> performanceValueList = new ArrayList<PerformanceValue>();

				performanceValueList = personalEvaluationService.getTotalScoreList(
						(String) request.getSession().getAttribute("employeId"),
						(Integer) request.getSession().getAttribute("authLevel"),
						yyyyMM);

				request.setAttribute("performanceValueList", performanceValueList);
				logger.debug("performanceListData | OUT | performanceValueListSize => " + performanceValueList.size());
			} else {
				throw new GlobalException("Failed", "현재 월 이후의 데이터는 조회가 불가합니다.");
			}
		} catch (GlobalException e) {
			throw e;
		}

		return "/sub/performance/performanceListData";
	}

	/**
	 * 성과/가치평가 조회 데이터 리스트(Admin)
	 * 
	 * @param yyyyMM
	 * @param searchOption
	 * @param request
	 * @param model
	 * @return 성과/가치평가 조회 데이터 리스트 View
	 * @attribute performanceValueList : 평가리스트
	 */
	@RequestMapping(value = "/performanceListDataAdmin")
	public String performanceListDataAdmin(@RequestParam String yyyyMM,
			@RequestParam String searchOption,
			HttpServletRequest request,
			Model model) {

		logger.debug("performanceListData searchSDate=>" + yyyyMM + "session=>"
				+ request.getSession().getAttribute("authLevel"));

		List<PerformanceValue> performanceValueList = new ArrayList<PerformanceValue>();

		performanceValueList = personalEvaluationService.getTotalScoreList(
				(String) request.getSession().getAttribute("employeId"),
				(Integer) request.getSession().getAttribute("authLevel"),
				yyyyMM);

		request.setAttribute("performanceValueList", performanceValueList);

		logger.debug("performanceListData | OUT | performanceValueListSize => " + performanceValueList.size());
		return "/sub/performance/performanceListDataAdmin";
	}

	/**
	 * 성과평가 1차 승인 화면
	 * 
	 * @param employeId
	 * @param yyyyMM
	 * @param authLevel
	 * @param request
	 * @param model
	 * @return view url
	 * @attribute 기존 평가정보(from DB)
	 */
	@RequestMapping(value = "/performanceConfirm1")
	public String performanceStep1(@RequestParam String employeId,
			@RequestParam String yyyyMM,
			@RequestParam int authLevel,
			HttpServletRequest request,
			Model model) {
		PerformanceValue performanceValue = new PerformanceValue();

		performanceValue = personalEvaluationService.getTotalScore(employeId,
				authLevel,
				yyyyMM);

		request.setAttribute("performanceValue", performanceValue);
		return "/sub/performance/performanceConfirm1";
	}

	/**
	 * 성과평가 2차 승인 화면
	 * 
	 * @param employeId
	 * @param yyyyMM
	 * @param authLevel
	 * @param request
	 * @param model
	 * @return view url
	 * @attribute 기존 평가정보(from DB)
	 */
	@RequestMapping(value = "/performanceConfirm2")
	public String performanceStep2(@RequestParam String employeId,
			@RequestParam String yyyyMM,
			@RequestParam int authLevel,
			HttpServletRequest request,
			Model model) {
		PerformanceValue performanceValue = new PerformanceValue();

		performanceValue = personalEvaluationService.getTotalScore(employeId,
				authLevel,
				yyyyMM);

		request.setAttribute("performanceValue", performanceValue);
		return "/sub/performance/performanceConfirm2";
	}

	/**
	 * 성과평가 세부 데이터
	 * 
	 * @param employeId
	 * @param yyyyMM
	 * @param request
	 * @param model
	 * @return
	 * @attibutes 개별데이터
	 */
	@RequestMapping(value = "/performanceData")
	public String performanceData(@RequestParam String employeId,
			@RequestParam String yyyyMM,
			HttpServletRequest request,
			Model model) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		resultMap = personalEvaluationService.getTotalScoreMap(employeId,
				(Integer) request.getSession().getAttribute("authLevel"),
				yyyyMM);
		ResultPerformanceScore resPerfom = (ResultPerformanceScore) resultMap.get("performanceScore");

		request.setAttribute("performanceScore", resPerfom);
		request.setAttribute("valueScore1", resultMap.get("valueScore1"));
		request.setAttribute("valueScore2", resultMap.get("valueScore2"));
		request.setAttribute("comments1", (String) resultMap.get("comments1"));
		request.setAttribute("comments2", (String) resultMap.get("comments2"));
		request.setAttribute("valueRatio", (Double) resultMap.get("valueRatio"));
		request.setAttribute("performRatio", (Double) resultMap.get("performRatio"));
		request.setAttribute("performScoreHistory1", (Double) resultMap.get("performScoreHistory1"));
		request.setAttribute("valueScoreHistory1", (Double) resultMap.get("valueScoreHistory1"));
		request.setAttribute("performScoreHistory2", (Double) resultMap.get("performScoreHistory2"));
		request.setAttribute("valueScoreHistory2", (Double) resultMap.get("valueScoreHistory2"));

		return "/sub/performance/performanceData";
	}

	/**
	 * 성과평가 세부 데이터(Admin)
	 * 
	 * @param employeId
	 * @param yyyyMM
	 * @param request
	 * @param model
	 * @return
	 * @attibutes 개별데이터
	 */
	@RequestMapping(value = "/performanceDataAdmin")
	public String performanceDataAdmin(@RequestParam String employeId,
			@RequestParam String yyyyMM,
			HttpServletRequest request,
			Model model) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		resultMap = personalEvaluationService.getTotalScoreMap(employeId,
				(Integer) request.getSession().getAttribute("authLevel"),
				yyyyMM);
		ResultPerformanceScore resPerfom = (ResultPerformanceScore) resultMap.get("performanceScore");

		request.setAttribute("performanceScore", resPerfom);
		request.setAttribute("month", yyyyMM);
		request.setAttribute("valueScore1", resultMap.get("valueScore1"));
		request.setAttribute("valueScore2", resultMap.get("valueScore2"));
		request.setAttribute("comments1", (String) resultMap.get("comments1"));
		request.setAttribute("comments2", (String) resultMap.get("comments2"));
		request.setAttribute("valueRatio", (Double) resultMap.get("valueRatio"));
		request.setAttribute("performRatio", (Double) resultMap.get("performRatio"));
		request.setAttribute("performScoreHistory1", (Double) resultMap.get("performScoreHistory1"));
		request.setAttribute("valueScoreHistory1", (Double) resultMap.get("valueScoreHistory1"));
		request.setAttribute("performScoreHistory2", (Double) resultMap.get("performScoreHistory2"));
		request.setAttribute("valueScoreHistory2", (Double) resultMap.get("valueScoreHistory2"));

		return "/sub/performance/performanceDataAdmin";
	}

	/**
	 * 성과평가 저장
	 * 
	 * @param employeId
	 * @param yyyyMM
	 * @param performScore
	 * @param confirmNumber
	 * @param valueBoolean
	 * @param valueComments
	 * @param performComments
	 * @param dissentComments
	 * @param request
	 * @param model
	 * @return S : 성공, F : 실패
	 */
	@RequestMapping(value = "/savePerformScore", produces = "application/text; charset=euc-kr")
	@ResponseBody
	public String savePerformData(@RequestParam("employeId") String employeId,
			@RequestParam("month") String yyyyMM,
			@RequestParam("performScore") int performScore,
			@RequestParam("confirmNumber") int confirmNumber,
			@RequestParam("valueScore[]") List<Boolean> valueBoolean,
			@RequestParam(value = "valueComments", defaultValue = "") String valueComments,
			@RequestParam(value = "performComments", defaultValue = "") String performComments,
			@RequestParam(value = "dissentComments", defaultValue = "") String dissentComments,
			HttpServletRequest request,
			Model model) {
		logger.info("savePerformScore | START | confirmNumber=" + confirmNumber);
		PerformanceValue performanceValue = new PerformanceValue();
		String res = "";
		boolean result = false;

		performanceValue.setEmployeValues(employeId, "", "", "", confirmNumber, "", "");
		if (confirmNumber == 1 || employeId.equals("990101")) {
			performanceValue.setPerfomValues(performScore, 0, yyyyMM);
			performanceValue.setValueComments1(valueComments);
			performanceValue.setPerformComments1(performComments);
			performanceValue.setValueScore1(valueBoolean);
			performanceValue.setDissentComments1(dissentComments);
		}

		if (confirmNumber == 2 || employeId.equals("990101")) {
			performanceValue.setPerfomValues(0, performScore, yyyyMM);
			performanceValue.setValueComments2(valueComments);
			performanceValue.setPerformComments2(performComments);
			performanceValue.setValueScore2(valueBoolean);
			performanceValue.setDissentComments2(dissentComments);
		}

		if (employeId.equals("990101")) {
			performanceValue.setPerfomValues(performScore, performScore, yyyyMM);
		}

		if (employeId.equals("990101")) {
			result = personalEvaluationService.saveTotalScoreCEO(performanceValue);
		} else {
			result = personalEvaluationService.saveTotalScore(performanceValue, confirmNumber);
		}

		if (result) {
			res = "S";
		} else {
			res = "F";
		}

		logger.debug("confirmNumber=>" + confirmNumber);
		logger.info("savePerformScore | END |");

		return res;
	}

	/**
	 * 성과평가 저장(Admin)
	 * 
	 * @param employeId
	 * @param yyyyMM
	 * @param performScore
	 * @param valueBoolean1
	 * @param valueBoolean2
	 * @param valueComments
	 * @param performComments
	 * @param dissentComments
	 * @param request
	 * @param model
	 * @return S : 성공, F : 실패
	 */
	@RequestMapping(value = "/savePerformScoreAdmin", produces = "application/text; charset=euc-kr")
	@ResponseBody
	public String savePerformDataAdmin(@RequestParam("employeId") String employeId,
			@RequestParam("month") String yyyyMM,
			@RequestParam("performScore[]") List<Integer> performScore,
			@RequestParam("values1[]") List<Boolean> valueBoolean1,
			@RequestParam("values2[]") List<Boolean> valueBoolean2,
			@RequestParam(value = "valueComments[]", defaultValue = "") List<String> valueComments,
			@RequestParam(value = "performComments[]", defaultValue = "") List<String> performComments,
			@RequestParam(value = "dissentComments[]", defaultValue = "") List<String> dissentComments,
			HttpServletRequest request,
			Model model) {
		logger.info("savePerformScoreAdmin | START | employeId=" + employeId);
		PerformanceValue performanceValue = new PerformanceValue();
		String res = "";
		boolean result = false;
		int index = 0;
		for (int confirmNumber = 1; confirmNumber <= performScore.size(); confirmNumber++) {
			index = (confirmNumber - 1);
			performanceValue.setEmployeValues(employeId, "", "", "", confirmNumber, "", "");
			if (confirmNumber == 1) {
				performanceValue.setPerfomValues(performScore.get(index), 0, yyyyMM);
				performanceValue.setValueComments1(valueComments.get(index));
				performanceValue.setPerformComments1(performComments.get(index));
				performanceValue.setValueScore1(valueBoolean1);
				performanceValue.setDissentComments1(dissentComments.get(index));
			}

			if (confirmNumber == 2) {
				performanceValue.setPerfomValues(0, performScore.get(index), yyyyMM);
				performanceValue.setValueComments2(valueComments.get(index));
				performanceValue.setPerformComments2(performComments.get(index));
				performanceValue.setValueScore2(valueBoolean2);
				performanceValue.setDissentComments2(dissentComments.get(index));
			}

			result = personalEvaluationService.saveTotalScore(performanceValue, confirmNumber);

			if (result) {
				res = "S";
			} else {
				res = "F";
			}
		}
		logger.info("savePerformScoreAdmin | END | result : " + result);

		return res;
	}

	/**
	 * 성과평가 이의제기 저장
	 * 
	 * @param employeId
	 * @param yyyyMM
	 * @param dissent
	 * @param authLevel
	 * @param request
	 * @return S : 성공, F : 실패
	 */
	@RequestMapping(value = "/savePerformDissent")
	@ResponseBody
	public String savePerformDissent(@RequestParam(value = "employeId") String employeId,
			@RequestParam(value = "month") String yyyyMM,
			@RequestParam(value = "dissent") String dissent,
			@RequestParam(value = "useAuthLevel") int authLevel,
			HttpServletRequest request) {
		PerformanceScore performanceScore = new PerformanceScore();
		String res;

		performanceScore.setEmployeId(employeId);
		performanceScore.setMonth(yyyyMM);
		performanceScore.setDissent(dissent);
		performanceScore.setConfirmNumber(authLevel);

		if (personalEvaluationService.savePerformDissent(performanceScore)) {
			res = "S";
		} else {
			res = "F";
		}
		return res;
	}

	@RequestMapping(value = "/statstics")
	public String statistics() {
		return "/sub/statstics/statstics";
	}

	/**
	 * 평가 유효기간 얻기
	 * 
	 * @param currentDate
	 * @param addWorkDate
	 * @return 해당 월의 유효일자
	 */
	private Date getLimitDate(Date currentDate, int addWorkDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();

		cal.setTime(currentDate);
		cal.set(Calendar.DATE, 1);
		Date firstDayOfMonth = cal.getTime(); // 당월 1일
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 당월 1일의 요일(1 ~ 7 : SUNDAY ~ SATURDAY)
		logger.debug("First day of month : {}, Day of week : {}", sdf.format(firstDayOfMonth), dayOfWeek);

		int addDay = ((dayOfWeek + addWorkDate - 1) / 5) * 2; // 주말일수
		cal.add(Calendar.DATE, addWorkDate + addDay - 1);
		Date limitDayOfMonth = cal.getTime(); // 8 영업일 적용 일자
		logger.debug("Limit day of month : {}, Day of week : {}", sdf.format(limitDayOfMonth),
				cal.get(Calendar.DAY_OF_WEEK));

		return limitDayOfMonth;
	}

}
