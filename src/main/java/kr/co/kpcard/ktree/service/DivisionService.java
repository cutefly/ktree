package kr.co.kpcard.ktree.service;

import kr.co.kpcard.ktree.dao.PersonalEvaluationDao;
import kr.co.kpcard.ktree.domain.DivisionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DivisionService {

    private final PersonalEvaluationDao personalEvaluationDao;

    public List<DivisionInfo> getDivisionList() {
        return personalEvaluationDao.getDivisionList();
    }
}
