package com.ds.mall.user.service.impl;

import com.ds.mall.common.exception.BusinessException;
import com.ds.mall.common.result.ResultCode;
import com.ds.mall.common.security.SecurityUtils;
import com.ds.mall.user.dto.ProfileUpdateRequest;
import com.ds.mall.user.entity.UserAccount;
import com.ds.mall.user.mapper.RoleMapper;
import com.ds.mall.user.mapper.UserAccountMapper;
import com.ds.mall.user.service.UserService;
import com.ds.mall.user.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserAccountMapper userAccountMapper;
    private final RoleMapper roleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoVO updateProfile(ProfileUpdateRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        UserAccount user = userAccountMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setAvatar(request.getAvatar());
        userAccountMapper.updateById(user);

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
