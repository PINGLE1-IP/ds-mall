package com.ds.mall.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "登录响应")
public class LoginResponse {

    @Schema(description = "访问令牌")
    private String accessToken;
    private Long userId;
    private String username;
    private String nickname;
    private List<String> roles;
}
