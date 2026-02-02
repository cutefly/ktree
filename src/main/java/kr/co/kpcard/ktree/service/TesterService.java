package kr.co.kpcard.ktree.service;

import kr.co.kpcard.ktree.dao.TesterDao;
import kr.co.kpcard.ktree.domain.Tester;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TesterService {

    private final TesterDao testerDao;

    public List<Tester> getAllTesters() {
        return testerDao.findAllTesters();
    }

    public Tester getTesterByTesterId(String testerId) {
        return testerDao.findByTesterId(testerId).orElse(null);
    }
}
