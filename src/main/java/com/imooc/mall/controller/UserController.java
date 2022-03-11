package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.UserMapper;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
  @Autowired UserService userService;

  @GetMapping("/test")
  @ResponseBody
  public User personalPage() {
    return userService.getUser();
  }

  @PostMapping("/register")
  @ResponseBody
  public ApiRestResponse register(
      @RequestParam("userName") String userName, @RequestParam("password") String password)
      throws ImoocMallException {
    if (ObjectUtils.isEmpty(userName)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
    }
    if (ObjectUtils.isEmpty(password)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
    }
    if (password.length() < 8) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.PASSWORD_TOO_SHORT);
    }
    userService.register(userName, password);
    return ApiRestResponse.success();
  }

  @PostMapping("/user/login")
  @ResponseBody
  public ApiRestResponse login(
      @RequestParam("userName") String userName,
      @RequestParam("password") String password,
      HttpSession session)
      throws ImoocMallException {
    if (ObjectUtils.isEmpty(userName)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
    }
    if (ObjectUtils.isEmpty(password)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
    }

    User user = userService.login(userName, password);
    user.setPassword(null);
    session.setAttribute(Constant.IMOOC_MALL_USER,user);
    return ApiRestResponse.success(user);
  }

  @PostMapping("/user/update")
  @ResponseBody
  public ApiRestResponse updateUserInfo(HttpSession session, @RequestParam("signature") String signature) throws ImoocMallException {
    User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
    if (currentUser == null) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
    }
    User user = new User();
    user.setId(currentUser.getId());
    user.setPersonalizedSignature(signature);
    userService.updateInformation(user);
    return ApiRestResponse.success();
  }

  @PostMapping("/user/logout")
  @ResponseBody
  public ApiRestResponse logout(HttpSession session) {
    session.removeAttribute(Constant.IMOOC_MALL_USER);
    return ApiRestResponse.success();
  }

  @PostMapping("/admin/login")
  @ResponseBody
  public ApiRestResponse adminLogin(
          @RequestParam("userName") String userName,
          @RequestParam("password") String password,
          HttpSession session)
          throws ImoocMallException {
    if (ObjectUtils.isEmpty(userName)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
    }
    if (ObjectUtils.isEmpty(password)) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
    }

    User user = userService.login(userName, password);
    if (userService.checkAdminRole(user)) {
      user.setPassword(null);
      session.setAttribute(Constant.IMOOC_MALL_USER,user);
      return ApiRestResponse.success(user);
    } else {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
    }
  }
}
