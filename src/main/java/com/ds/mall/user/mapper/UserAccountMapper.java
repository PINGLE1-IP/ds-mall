package com.ds.mall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ds.mall.user.entity.UserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccount> {

    @Select("select * from user_account where username = #{username} and deleted = 0 limit 1")
    UserAccount selectByUsername(@Param("username") String username);
}
