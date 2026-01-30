package kr.co.kpcard.ktree.service;

import kr.co.kpcard.ktree.dao.EmployeeDao;
import kr.co.kpcard.ktree.domain.Employee;
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

    private final EmployeeDao employeeDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String employeId) throws UsernameNotFoundException {
        Optional<Employee> employeeOptional = employeeDao.findByEmployeId(employeId);

        Employee employee = employeeOptional
                .orElseThrow(() -> new UsernameNotFoundException("User not found with employeeId: " + employeId));

        // Corrected accessor methods for record
        String username = employee.employeId();
        String password = employee.pwd();

        // Fetch authority_level
        Optional<Integer> authorityLevelOptional = employeeDao.findAuthorityLevelByEmployeId(username);
        Integer authorityLevel = authorityLevelOptional.orElse(0); // Default to 0 if not found

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (authorityLevel == 4) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return User.builder()
                .username(username)
                // Password from DB is assumed to be plain text here, so encode it.
                // In a real application, passwords in DB should be stored as encoded strings.
                .password(passwordEncoder.encode(password))
                .authorities(authorities)
                .build();
    }
}
