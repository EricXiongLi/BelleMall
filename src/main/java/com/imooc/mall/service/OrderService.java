package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.VO.OrderVO;
import com.imooc.mall.model.request.CreateOrderReq;

import java.io.IOException;

public interface OrderService {
    String create(CreateOrderReq createOrderReq) throws ImoocMallException;

    OrderVO detail(String orderNo) throws ImoocMallException;

    PageInfo listForCustomer(Integer pageNum, Integer pageSize) throws ImoocMallException;

    void cancel(String orderNo) throws ImoocMallException;

    String qrcode(String orderNo) throws IOException, WriterException;
}
