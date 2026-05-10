package com.ds.mall.user.service;

import com.ds.mall.user.dto.LoginRequest;
import com.ds.mall.user.dto.RegisterRequest;
import com.ds.mall.user.vo.LoginResponse;
import com.ds.mall.user.vo.UserInfoVO;

public interface AuthService {

    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    UserInfoVO currentUser();
}
