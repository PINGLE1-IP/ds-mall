package com.ds.mall.common.security;

import com.ds.mall.common.exception.BusinessException;
import com.ds.mall.common.result.ResultCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Long getCurrentUserId() {
        return getLoginUser().getUserId();
    }

    public static boolean isAdmin() {
        return getLoginUser().getRoles().contains("ADMIN");
    }

    public static LoginUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUser loginUser)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return loginUser;
    }
}
