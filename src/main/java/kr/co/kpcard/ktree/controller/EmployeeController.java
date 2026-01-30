package kr.co.kpcard.ktree.controller;

import kr.co.kpcard.ktree.domain.Employee;
import kr.co.kpcard.ktree.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeService.getAllEmployees();
    }
}
