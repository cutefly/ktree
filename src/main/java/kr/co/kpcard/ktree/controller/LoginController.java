package kr.co.kpcard.ktree.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        log.info("Accessing login page");
        return "login";
    }
}
