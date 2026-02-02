package kr.co.kpcard.ktree.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Update("UPDATE kpc_employe SET pwd = #{password} WHERE employe_id = #{employeId}")
    int updatePassword(@Param("employeId") String employeId, @Param("password") String password);

}
