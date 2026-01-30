package kr.co.kpcard.ktree.dao;

import kr.co.kpcard.ktree.domain.Employee;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeDao {

    private final SqlSession sqlSession;

    public List<Employee> findAllEmployees() {
        return sqlSession.selectList("Personal.findAllEmployees");
    }
}
