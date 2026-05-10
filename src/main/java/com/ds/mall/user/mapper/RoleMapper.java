package com.ds.mall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ds.mall.user.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("""
            select r.role_code
            from role r
            inner join user_role ur on ur.role_id = r.id
            where ur.user_id = #{userId} and r.deleted = 0
            """)
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);

    @Select("select * from role where role_code = #{roleCode} and deleted = 0 limit 1")
    Role selectByRoleCode(@Param("roleCode") String roleCode);
}
