package kr.co.kpcard.ktree.utility;

import java.util.List;

import kr.co.kpcard.ktree.domain.ProjectScore;
import kr.co.kpcard.ktree.domain.ResultProjectScore;

public class ProjectUtil {
    public static boolean calcUseAuth(String sessionId, ResultProjectScore resultProjectScore) {
        boolean useAuth = false;
        if (sessionId.equals(resultProjectScore.getEmployeId()) && resultProjectScore.getStatus() == 0) {
            useAuth = true;
        } else if (sessionId.equals(resultProjectScore.getConfirm1()) && resultProjectScore.getStatus() == 1) {
            useAuth = true;
        } else if (sessionId.equals(resultProjectScore.getConfirm2()) && resultProjectScore.getStatus() == 2) {
            useAuth = true;
        }
        return useAuth;
    }

    public static boolean calcUseDissent(String sessionId,
            ResultProjectScore resultProjectScore) {

        boolean useDissent = false;

        if (sessionId.equals(resultProjectScore.getEmployeId()) && resultProjectScore.getStatus() >= 3) {
            useDissent = true;
        } else if (sessionId.equals(resultProjectScore.getConfirm1()) && resultProjectScore.getStatus() == 4) {
            useDissent = true;
        } else if (sessionId.equals(resultProjectScore.getConfirm2()) && resultProjectScore.getStatus() == 5) {
            useDissent = true;
        } else if ((sessionId.equals(resultProjectScore.getEmployeId()) ||
                sessionId.equals(resultProjectScore.getConfirm1()) ||
                sessionId.equals(resultProjectScore.getConfirm2())) && resultProjectScore.getStatus() == 6) {
            useDissent = true;
        }
        return useDissent;
    }

    public static int calcAuthLevel(String sessionId, ResultProjectScore resultProjectScore) {
        int authLevel = 0;

        authLevel = (sessionId.equals(resultProjectScore.getConfirm1())) ? 1
                : (sessionId.equals(resultProjectScore.getConfirm2())) ? 2 : 0;

        return authLevel;
    }

    public static boolean calcEvaluable(String sessionId, ResultProjectScore resultProjectScore) {
        // 평가자 여부
        boolean evaluable = false;

        boolean condition0 = sessionId.equals(resultProjectScore.getEmployeId())
                && (resultProjectScore.getStatus() == 0);
        boolean condition1 = sessionId.equals(resultProjectScore.getConfirm1())
                && (resultProjectScore.getStatus() == 1);
        boolean condition2 = sessionId.equals(resultProjectScore.getConfirm2())
                && (resultProjectScore.getStatus() == 2);

        evaluable = (condition0 || condition1 || condition2) && (resultProjectScore.getStatus() < 3);
        return evaluable;
    }

    // (session.employeId == projectScore.employeId and projectScore.status >= 3) or
    // (projectScore.status == 4 and session.employeId == projectScore.confirm1) or
    // (projectScore.status == 5 and session.employeId == projectScore.confirm2) or
    // ((session.employeId == projectScore.employeId or session.employeId ==
    // projectScore.confirm1 or session.employeId == projectScore.confirm2) and
    // projectScore.status == 6))
    public static boolean calcDissentable(String sessionId, ResultProjectScore resultProjectScore) {
        // 이의제기자 여부
        boolean dissentable = false;

        boolean condition0 = sessionId.equals(resultProjectScore.getEmployeId())
                && (resultProjectScore.getStatus() >= 3);
        boolean condition1 = sessionId.equals(resultProjectScore.getConfirm1())
                && (resultProjectScore.getStatus() == 4);
        boolean condition2 = sessionId.equals(resultProjectScore.getConfirm2())
                && (resultProjectScore.getStatus() == 5);
        boolean condition3 = (sessionId.equals(resultProjectScore.getEmployeId())
                || sessionId.equals(resultProjectScore.getConfirm1())
                || sessionId.equals(resultProjectScore.getConfirm2())) && (resultProjectScore.getStatus() == 6);

        dissentable = condition0 || condition1 || condition2 || condition3;

        return dissentable;
    }

    public static List<Float> calcMyScores(String employeId, List<ProjectScore> scoreHistList) {
        List<Float> myScores = List.of(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        for (ProjectScore scoreHist : scoreHistList) {
            if (employeId.equals(scoreHist.getEmployeId())) {
                myScores = List.of(scoreHist.getScore1(), scoreHist.getScore2(), scoreHist.getScore3(),
                        scoreHist.getScore4(), scoreHist.getScore5(), scoreHist.getScore6(),
                        scoreHist.getScore7(), scoreHist.getScore8());
                break;
            }
        }
        return myScores;
    }
}
