package kr.co.kpcard.ktree.mapper;

import kr.co.kpcard.ktree.domain.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    @Select("SELECT seq, employe_id, name, pwd, authority_code, create_date, use_yn, team_code, division_code, position, confirm1, confirm2 FROM kpc_employe")
    List<Employee> findAll();
}
