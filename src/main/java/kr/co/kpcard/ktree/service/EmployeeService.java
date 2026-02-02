package kr.co.kpcard.ktree.service;

import kr.co.kpcard.ktree.dao.EmployeeDao;
import kr.co.kpcard.ktree.domain.Employee;
import lombok.RequiredArgsConstructor;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeDao employeeDao;
    // private final PasswordEncoder passwordEncoder;

    public List<Employee> getAllEmployees() {
        return employeeDao.findAllEmployees();
    }

    public Employee getEmployeeByEmployeId(String employeId) {
        return employeeDao.findByEmployeId(employeId).orElse(null);
    }

    @Transactional
    public boolean changePassword(String employeId, String currentPassword, String newPassword) {
        Employee employee = getEmployeeByEmployeId(employeId);
        // if (employee != null && passwordEncoder.matches(currentPassword,
        // employee.pwd())) {
        // String encodedNewPassword = passwordEncoder.encode(newPassword);
        // employeeDao.updatePassword(employeId, encodedNewPassword);
        // return true;
        // }
        if (employee != null && currentPassword.equals(employee.pwd())) {
            employeeDao.updatePassword(employeId, newPassword);
            return true;
        }
        return false;
    }
}
