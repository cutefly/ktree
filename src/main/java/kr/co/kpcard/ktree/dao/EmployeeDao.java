package kr.co.kpcard.ktree.dao;

import kr.co.kpcard.ktree.domain.Employee;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeeDao {

    private final SqlSession sqlSession;

    public List<Employee> findAllEmployees() {
        return sqlSession.selectList("Personal.findAllEmployees");
    }

    public Optional<Employee> findByEmployeId(String employeId) {
        return Optional.ofNullable(sqlSession.selectOne("Personal.findByEmployeId", employeId));
    }

    public Optional<Integer> findAuthorityLevelByEmployeId(String employeId) {
        return Optional.ofNullable(sqlSession.selectOne("Personal.findAuthorityLevelByEmployeId", employeId));
    }
}
