package kr.co.kpcard.ktree.dao;

import kr.co.kpcard.ktree.domain.Tester;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TesterDao {

    private final SqlSession sqlSession;

    public List<Tester> findAllTesters() {
        return sqlSession.selectList("Tester.findAllTesters");
    }

    public Optional<Tester> findByTesterId(String testerId) {
        return Optional.ofNullable(sqlSession.selectOne("Tester.findByTesterId", testerId));
    }
}
