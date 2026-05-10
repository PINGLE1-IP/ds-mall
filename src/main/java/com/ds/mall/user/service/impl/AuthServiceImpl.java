package com.ds.mall.user.service.impl;

import com.ds.mall.common.exception.BusinessException;
import com.ds.mall.common.result.ResultCode;
import com.ds.mall.common.security.JwtTokenService;
import com.ds.mall.common.security.LoginUser;
import com.ds.mall.common.security.SecurityUtils;
import com.ds.mall.user.dto.LoginRequest;
import com.ds.mall.user.dto.RegisterRequest;
import com.ds.mall.user.entity.Role;
import com.ds.mall.user.entity.UserAccount;
import com.ds.mall.user.entity.UserRole;
import com.ds.mall.user.mapper.RoleMapper;
import com.ds.mall.user.mapper.UserAccountMapper;
import com.ds.mall.user.mapper.UserRoleMapper;
import com.ds.mall.user.service.AuthService;
import com.ds.mall.user.vo.LoginResponse;
import com.ds.mall.user.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserAccountMapper userAccountMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterRequest request) {
        if (userAccountMapper.selectByUsername(request.getUsername()) != null) {
            throw new BusinessException(ResultCode.REPEAT_OPERATION, "用户名已存在");
        }
        UserAccount user = new UserAccount();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        user.setStatus(1);
        userAccountMapper.insert(user);

        Role role = roleMapper.selectByRoleCode("USER");
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());
        userRoleMapper.insert(userRole);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        UserAccount user = userAccountMapper.selectByUsername(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
        List<String> roles = roleMapper.selectRoleCodesByUserId(user.getId());
        LoginUser loginUser = new LoginUser(user.getId(), user.getUsername(), user.getPassword(), user.getStatus(), roles);
        LoginResponse response = new LoginResponse();
        response.setAccessToken(jwtTokenService.createToken(loginUser));
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setRoles(roles);
        return response;
    }

    @Override
    public UserInfoVO currentUser() {
        Long userId = SecurityUtils.getCurrentUserId();
        UserAccount user = userAccountMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        UserInfoVO vo = new UserInfoVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        vo.setAvatar(user.getAvatar());
        vo.setStatus(user.getStatus());
        vo.setRoles(roleMapper.selectRoleCodesByUserId(user.getId()));
        return vo;
    }
}
