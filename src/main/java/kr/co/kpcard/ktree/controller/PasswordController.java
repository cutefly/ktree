package kr.co.kpcard.ktree.controller;

import kr.co.kpcard.ktree.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PasswordController {

    private final UserInfoService userInfoService;

    @GetMapping("/password")
    public String showPasswordChangePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute("employeId", userDetails.getUsername());
        }
        return "password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("employeId") String employeId,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmNewPassword") String confirmNewPassword,
            Model model) {

        log.debug("현재 비밀번호 : {}, 새로운 비밀번호 : {}", oldPassword, newPassword);
        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute("error", "새로운 비밀번호가 일치하지 않습니다.");
            model.addAttribute("employeId", employeId);
            return "password";
        }

        if (newPassword.equals(oldPassword)) {
            model.addAttribute("error", "현재 비밀번호와 새로운 비밀번호가 동일합니다.");
            model.addAttribute("employeId", employeId);
            return "password";
        }

        // 새로운 비밀번호
        boolean success = userInfoService.changePassword(employeId, oldPassword, newPassword);

        if (success) {
            model.addAttribute("success", "비밀번호가 정상적으로 변경이 되었습니다.");
        } else {
            model.addAttribute("error", "비밀번호 변경 오류가 있습니다. 현재 비밀번호를 확인해 주세요.");
        }
        model.addAttribute("employeId", employeId);
        return "password";
    }

    /**
     * 직원 비밀번호 초기화
     * 
     * @param employeId
     * @param session
     * @return S: 성공, F: 실패
     */
    @RequestMapping(value = "/reset-password", produces = "application/text; charset=euc-kr")
    @ResponseBody
    public String resetPassword(@RequestParam("employeId") String employeId,
            jakarta.servlet.http.HttpSession session) {
        log.info("resetPassword | IN | employeId : {}", employeId);
        String res = "F";
        try {
            if (session.getAttribute("employeId").equals("kpc_admin")) {
                boolean result = userInfoService.resetPassword(employeId);
                if (result) {
                    res = "S";
                }
            } else {
                log.warn("Unauthorized attempt to reset password for employeId: {}", employeId);
            }
        } catch (Exception e) {
            log.error("Error resetting password for employeId: {}", employeId, e);
        }
        log.info("resetPassword | OUT | result : {}", res);
        return res;
    }
}
