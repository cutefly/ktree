package kr.co.kpcard.ktree.service;

import kr.co.kpcard.ktree.app.enums.AuthLevel;
import kr.co.kpcard.ktree.dao.UserInfoDao;
import kr.co.kpcard.ktree.domain.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserInfoDao userInfoDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<UserInfo> userInfoOptional = userInfoDao.findByUserId(userId);

        UserInfo userInfo = userInfoOptional
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));

        // Corrected accessor methods for record
        String username = userInfo.employeId();
        String password = userInfo.pwd();

        // Fetch authority_level
        Optional<Integer> authorityLevelOptional = userInfoDao.findAuthorityLevelByUserId(username);
        Integer authorityLevel = authorityLevelOptional.orElse(0); // Default to 0 if not found

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (authorityLevel == AuthLevel.ADMIN.getCode()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .authorities(authorities)
                .build();
    }
}
