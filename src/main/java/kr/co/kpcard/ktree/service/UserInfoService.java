package kr.co.kpcard.ktree.service;

import kr.co.kpcard.ktree.dao.UserInfoDao;
import kr.co.kpcard.ktree.domain.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserInfoService {

    private final UserInfoDao userInfoDao;

    public UserInfo getUserByUserId(String userId) {
        return userInfoDao.findByUserId(userId).orElse(null);
    }

    public Integer findAuthorityLevelByUserId(String userId) {
        return userInfoDao.findAuthorityLevelByUserId(userId).orElse(null);
    }

    @Transactional
    public boolean changePassword(String userId, String currentPassword, String newPassword) {
        UserInfo userInfo = getUserByUserId(userId);
        if (userInfo != null && currentPassword.equals(userInfo.pwd())) {
            // String encodedNewPassword = passwordEncoder.encode(newPassword);
            userInfoDao.updatePassword(userId, newPassword);
            return true;
        }
        return false;
    }

    public boolean resetPassword(String userId, String password) {
        UserInfo userInfo = getUserByUserId(userId);

        if (userInfo != null) {
            // String encodedNewPassword = passwordEncoder.encode(newPassword);
            userInfoDao.updatePassword(userId, password);
            return true;
        }
        return false;
    }
}
