package kr.co.kpcard.ktree.service;

import kr.co.kpcard.ktree.dao.PersonalEvaluationDao;
import kr.co.kpcard.ktree.domain.TeamInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final PersonalEvaluationDao personalEvaluationDao;

    public List<TeamInfo> getTeamList(int divisionCode) {
        return personalEvaluationDao.getTeamList(divisionCode);
    }
}
