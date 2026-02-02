package kr.co.kpcard.ktree.controller;

import kr.co.kpcard.ktree.domain.Tester;
import kr.co.kpcard.ktree.service.TesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TesterController {

    private final TesterService testerService;

    @GetMapping("/testers")
    public List<Tester> getTesters() {
        return testerService.getAllTesters();
    }

    @GetMapping("/tester/me")
    public Tester getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String testerId = userDetails.getUsername();
            return testerService.getTesterByTesterId(testerId);
        }
        return null;
    }
}
