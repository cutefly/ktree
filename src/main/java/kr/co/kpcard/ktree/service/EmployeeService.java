package kr.co.kpcard.ktree.service;

import kr.co.kpcard.ktree.domain.Employee;
import kr.co.kpcard.ktree.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeMapper employeeMapper;

    public List<Employee> getAllEmployees() {
        return employeeMapper.findAll();
    }
}
