package com.ds.mall.user.service;

import com.ds.mall.user.dto.ProfileUpdateRequest;
import com.ds.mall.user.vo.UserInfoVO;

public interface UserService {

    UserInfoVO updateProfile(ProfileUpdateRequest request);
}
