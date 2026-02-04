package kr.co.kpcard.ktree.utility;

import java.util.Date;
import java.util.List;

import kr.co.kpcard.ktree.domain.ProjectScore;
import kr.co.kpcard.ktree.domain.ResultProjectScore;

/*
 * Project 관련 Utility Class
 */
public class ProjectUtil {

    // 평가권한자 여부
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

    // 이의제기권한자 여부
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

    // 평가권한 레벨 계산
    public static int calcAuthLevel(String sessionId, ResultProjectScore resultProjectScore) {
        int authLevel = 0;

        authLevel = (sessionId.equals(resultProjectScore.getConfirm1())) ? 1
                : (sessionId.equals(resultProjectScore.getConfirm2())) ? 2 : 0;

        return authLevel;
    }

    // 평가자 여부
    public static boolean calcEvaluable(String sessionId, ResultProjectScore resultProjectScore) {
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

    // 이의제기자 여부
    public static boolean calcDissentable(String sessionId, ResultProjectScore resultProjectScore) {
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

    // 나의 점수 계산
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
