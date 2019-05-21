package com.wxmp.wxcms.mapper;

import com.wxmp.wxcms.domain.CallOrder;

import java.util.List;

public interface CallOrderDao {

    int insert(CallOrder callOrder);

    List<CallOrder> findOrderByOrderStatus(String userId);

}
