package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.VO.OrderVO;
import com.imooc.mall.model.request.CreateOrderReq;

public interface OrderService {
    String create(CreateOrderReq createOrderReq) throws ImoocMallException;

    OrderVO detail(String orderNo) throws ImoocMallException;

    PageInfo listForCustomer(Integer pageNum, Integer pageSize) throws ImoocMallException;
}
