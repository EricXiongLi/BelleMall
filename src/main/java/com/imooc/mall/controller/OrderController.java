package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.VO.OrderVO;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
  @Autowired OrderService orderService;

  @ApiOperation("create order")
  @PostMapping("/order/create")
  public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq)
      throws ImoocMallException {
    String orderNo = orderService.create(createOrderReq);
    return ApiRestResponse.success(orderNo);
  }

  @ApiOperation("detail of order")
  @GetMapping("/order/detail")
  public ApiRestResponse detail(@RequestParam String orderNo) throws ImoocMallException {
    OrderVO orderVO = orderService.detail(orderNo);
    return ApiRestResponse.success(orderVO);
  }

  @ApiOperation("order list")
  @GetMapping("/order/list")
  public ApiRestResponse list(@RequestParam Integer pageNum, Integer pageSize)
      throws ImoocMallException {
    PageInfo pageInfo = orderService.listForCustomer(pageNum, pageSize);
    return ApiRestResponse.success(pageInfo);
  }
}
