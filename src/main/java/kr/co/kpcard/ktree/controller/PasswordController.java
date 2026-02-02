package kr.co.kpcard.ktree.controller;

import kr.co.kpcard.ktree.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PasswordController {

    private final EmployeeService employeeService;

    @GetMapping("/password")
    public String showPasswordChangePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute("employeId", userDetails.getUsername());
        }
        return "password";
    }

    @PostMapping("/change-password")
    @ResponseBody
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("oldPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword) {
        log.debug("Password change request for user: {}", userDetails.getUsername());
        if (userDetails == null) {
            return "F";
        }
        log.debug("Current Password: {}, New Password: {}", currentPassword, newPassword);
        boolean success = employeeService.changePassword(userDetails.getUsername(), currentPassword, newPassword);
        return success ? "S" : "F";
    }
}
