package kr.co.kpcard.ktree.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.kpcard.ktree.domain.UserInfo;
import kr.co.kpcard.ktree.service.UserInfoService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserInfoService userInfoService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();
        UserInfo userInfo = userInfoService.getUserByUserId(username);
        Integer authLevel = userInfoService.findAuthorityLevelByUserId(username);

        if (userInfo != null) {
            HttpSession session = request.getSession();
            session.setAttribute("employeId", userInfo.employeId());
            session.setAttribute("employeName", userInfo.name());
            session.setAttribute("authLevel", authLevel);

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (!authorities.isEmpty()) {
                GrantedAuthority auth = authorities.iterator().next();
                session.setAttribute("authority", auth.getAuthority());
            }

        }

        response.sendRedirect("/");
    }
}
