package kr.co.kpcard.ktree.domain.extend;

import java.util.List;

import kr.co.kpcard.ktree.domain.ResultProjectScore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ResultProjectScoreExtend extends ResultProjectScore {
    private Integer userAuthLevel;
    private Integer useAuth;
    private Boolean useDissent;
    private List<Float> scoreList;
    private List<Float> myScoreList;

    public static ResultProjectScoreExtend copyFromParent(ResultProjectScore parent) {
        return ResultProjectScoreExtend.builder()
                .seq(parent.getSeq())
                .month(parent.getMonth())
                .employeId(parent.getEmployeId())
                .score1(parent.getScore1())
                .score2(parent.getScore2())
                .score3(parent.getScore3())
                .score4(parent.getScore4())
                .score5(parent.getScore5())
                .score6(parent.getScore6())
                .score7(parent.getScore7())
                .score8(parent.getScore8())
                .dissent(parent.getDissent())
                .comments1(parent.getComments1())
                .comments2(parent.getComments2())
                .confirmNumber(parent.getConfirmNumber())
                .build();
    }
}
