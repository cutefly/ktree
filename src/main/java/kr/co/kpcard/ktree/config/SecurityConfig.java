package kr.co.kpcard.ktree.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console; // Not used anymore

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final AuthenticationSuccessHandler authenticationSuccessHandler;

        public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler) {
                this.authenticationSuccessHandler = authenticationSuccessHandler;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
                                // .headers(headers -> headers.frameOptions(frameOptions ->
                                // frameOptions.sameOrigin())) // Enable
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**",
                                                                "/fonts/**", "/less/**",
                                                                "/scss/**")
                                                .permitAll() // Permit access to home, login page, static resources
                                                // .requestMatchers("/password", "/change-password").permitAll()
                                                .requestMatchers("/api/**",
                                                                "/eval/**")
                                                .authenticated() // Require authentication
                                                                 // for /api/**
                                                .anyRequest().authenticated() // All other requests require
                                                                              // authentication
                                )
                                .formLogin(formLogin -> formLogin
                                                .loginPage("/login") // Custom login page
                                                .loginProcessingUrl("/login-proc") // URL to submit username and
                                                                                   // password
                                                .successHandler(authenticationSuccessHandler) // Redirect to home page
                                                                                              // after successful login
                                                .failureUrl("/login?error") // Redirect to login page with error on
                                                                            // failed login
                                                .permitAll() // Allow everyone to access login related URLs
                                )
                                .logout(logout -> logout
                                                .logoutUrl("/logout") // URL for logout
                                                .logoutSuccessUrl("/login") // Redirect to login page after logout
                                                .invalidateHttpSession(true) // Invalidate HTTP session
                                                .deleteCookies("JSESSIONID") // Delete cookies
                                                .permitAll() // Allow everyone to access logout related URLs
                                );
                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
