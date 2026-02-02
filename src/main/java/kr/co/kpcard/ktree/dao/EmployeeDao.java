package kr.co.kpcard.ktree.dao;

import kr.co.kpcard.ktree.domain.Employee;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public void updatePassword(String employeId, String newPassword) {
        Map<String, String> map = new HashMap<>(); // <Key타입, Value타입>

        // 2. 데이터 추가 (put)
        map.put("employeId", employeId);
        map.put("newPassword", newPassword);

        sqlSession.update("Personal.updatePassword", map);
    }
}
