package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.VO.CartVO;
import com.imooc.mall.service.CartService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

  @Autowired CartService cartService;

  @ApiOperation("get cart list")
  @GetMapping("/list")
  public ApiRestResponse list() {
    List<CartVO> cartList = cartService.list(UserFilter.currentUser.getId());
    return ApiRestResponse.success(cartList);
  }

  @ApiOperation("add product to cart")
  @PostMapping("/add")
  public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count)
      throws ImoocMallException {
    List<CartVO> cartVOList = cartService.add(UserFilter.currentUser.getId(), productId, count);
    return ApiRestResponse.success(cartVOList);
  }
}
