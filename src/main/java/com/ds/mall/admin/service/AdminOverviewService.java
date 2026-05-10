package com.ds.mall.admin.service;

import com.ds.mall.admin.dto.AdminOverviewQuery;
import com.ds.mall.admin.vo.AdminOverviewVO;

public interface AdminOverviewService {

    AdminOverviewVO overview(AdminOverviewQuery query);
}
