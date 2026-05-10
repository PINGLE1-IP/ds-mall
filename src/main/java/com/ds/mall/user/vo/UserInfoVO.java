package com.ds.mall.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "用户信息")
public class UserInfoVO {

    private Long userId;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private Integer status;
    private List<String> roles;
}
