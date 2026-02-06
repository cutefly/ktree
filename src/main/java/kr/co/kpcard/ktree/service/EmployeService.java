package kr.co.kpcard.ktree.service;

import kr.co.kpcard.ktree.dao.PersonalEvaluationDao;
import kr.co.kpcard.ktree.domain.Employe;
import kr.co.kpcard.ktree.domain.EmployeInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeService {

    private final Logger logger = LoggerFactory.getLogger(EmployeService.class);
    private final PersonalEvaluationDao personalEvaluationDao;

    public List<Employe> getEmployeList(Map<String, Object> params) {
        logger.info("getEmployeList | IN ");
        List<Employe> employes = personalEvaluationDao.getUserList(params);
        logger.info("getEmployeList | OUT | " + employes.size());
        return employes;
    }
    public List<Employe> getEmployeList() {
        return getEmployeList(new java.util.HashMap<>());
    }

    public Employe getEmploye(Long seq) {
        logger.info("getEmploye | IN | seq: {}", seq);
        Employe employe = null;
        try {
            employe = personalEvaluationDao.getUser(seq);
        } catch (Exception e) {
            logger.error("Error retrieving employe with SEQ {}: {}", seq, e.getMessage());
        }
        logger.info("getEmploye | OUT | employe: {}", employe);
        return employe;
    }

    @Transactional
    public boolean addEmploye(Employe employe) {
        logger.info("addEmploye | IN | employe: {}", employe);
        boolean success = false;
        try {
            success = personalEvaluationDao.addUser(employe);
        } catch (Exception e) {
            logger.error("Error adding employe {}: {}", employe.getEmployeId(), e.getMessage());
        }
        logger.info("addEmploye | OUT | success: {}", success);
        return success;
    }

    @Transactional
    public boolean updateEmploye(Employe employe) {
        logger.info("updateEmploye | IN | employe: {}", employe);
        boolean success = false;
        try {
            success = personalEvaluationDao.updateUser(employe);
        } catch (Exception e) {
            logger.error("Error updating employe {}: {}", employe.getEmployeId(), e.getMessage());
        }
        logger.info("updateEmploye | OUT | success: {}", success);
        return success;
    }
}
