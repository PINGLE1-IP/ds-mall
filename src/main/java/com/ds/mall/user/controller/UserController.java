package com.ds.mall.user.controller;

import com.ds.mall.common.result.Result;
import com.ds.mall.user.dto.ProfileUpdateRequest;
import com.ds.mall.user.service.UserService;
import com.ds.mall.user.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户接口")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "修改个人信息")
    @PutMapping("/profile")
    public Result<UserInfoVO> updateProfile(@Valid @RequestBody ProfileUpdateRequest request) {
        return Result.success(userService.updateProfile(request));
    }
}
