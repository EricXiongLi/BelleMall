package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderAdminController {

  @Autowired OrderService orderService;

  @ApiOperation("order list for admin")
  @GetMapping("admin/order/list")
  public ApiRestResponse listForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize)
      throws ImoocMallException {
    PageInfo pageInfo = orderService.listForAdmin(pageNum, pageSize);
    return ApiRestResponse.success(pageInfo);
  }

  @ApiOperation("admin deliver order")
  @PostMapping("admin/order/delivered")
  public ApiRestResponse delivered(@RequestParam String orderNo) throws ImoocMallException {
    orderService.deliver(orderNo);
    return ApiRestResponse.success();
  }

  @PostMapping("order/finish")
  @ApiOperation("order finish")
  public ApiRestResponse finish(@RequestParam String orderNo) throws ImoocMallException {
    orderService.finish(orderNo);
    return ApiRestResponse.success();
  }
}
