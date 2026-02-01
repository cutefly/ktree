package kr.co.kpcard.ktree.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.kpcard.ktree.domain.Employee;
import kr.co.kpcard.ktree.service.EmployeeService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final EmployeeService employeeService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();
        Employee employee = employeeService.getEmployeeByEmployeId(username);

        if (employee != null) {
            HttpSession session = request.getSession();
            session.setAttribute("employeId", employee.employeId());
            session.setAttribute("employeName", employee.name());

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (!authorities.isEmpty()) {
                GrantedAuthority auth = authorities.iterator().next();
                session.setAttribute("authLevel", auth.getAuthority());
            }

        }

        response.sendRedirect("/");
    }
}
