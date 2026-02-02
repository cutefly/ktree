package kr.co.kpcard.ktree.dao;

import kr.co.kpcard.ktree.domain.UserInfo;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserInfoDao {

    private final SqlSession sqlSession;

    public Optional<UserInfo> findByUserId(String userId) {
        return Optional.ofNullable(sqlSession.selectOne("UserInfo.findByUserId", userId));
    }

    public Optional<Integer> findAuthorityLevelByUserId(String userId) {
        return Optional.ofNullable(sqlSession.selectOne("UserInfo.findAuthorityLevelByUserId", userId));
    }

    public void updatePassword(String userId, String newPassword) {
        Map<String, String> map = new HashMap<>(); // <Key타입, Value타입>

        // 2. 데이터 추가 (put)
        map.put("userId", userId);
        map.put("newPassword", newPassword);

        sqlSession.update("UserInfo.updatePassword", map);
    }
}
