package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.VO.CategoryVO;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.request.AddCategoryReq;
import com.imooc.mall.model.request.UpdateCategoryReq;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class CategoryController {
  @Autowired UserService userService;

  @Autowired CategoryService categoryService;

  @ApiOperation("add new category")
  @PostMapping("/admin/category/add")
  @ResponseBody
  public ApiRestResponse addCategory(
      HttpSession httpSession, @Valid @RequestBody AddCategoryReq addCategoryReq)
      throws ImoocMallException {
    User currentUser = (User) httpSession.getAttribute(Constant.IMOOC_MALL_USER);
    if (currentUser == null) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
    }
    boolean adminRole = userService.checkAdminRole(currentUser);
    if (adminRole) {
      categoryService.add(addCategoryReq);
      return ApiRestResponse.success();
    } else {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
    }
  }

  @ApiOperation("update category")
  @PostMapping("/admin/category/update")
  @ResponseBody
  public ApiRestResponse updateCategory(HttpSession httpSession,
                                        @Valid @RequestBody UpdateCategoryReq updateCategoryReq) throws ImoocMallException {

    User currentUser = (User) httpSession.getAttribute(Constant.IMOOC_MALL_USER);
    if (currentUser == null) {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
    }
    boolean adminRole = userService.checkAdminRole(currentUser);
    if (adminRole) {
      Category category = new Category();
      BeanUtils.copyProperties(updateCategoryReq, category);
      categoryService.update(category);
      return ApiRestResponse.success();
    } else {
      return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
    }
  }

  @ApiOperation("delete category in backend")
  @PostMapping("admin/category/delete")
  @ResponseBody
  public ApiRestResponse deleteCategory(@RequestParam Integer id) throws ImoocMallException {
    categoryService.delete(id);
    return ApiRestResponse.success();
  }

  @ApiOperation("category list in backend")
  @PostMapping("/admin/category/list")
  @ResponseBody
  public ApiRestResponse listCategoryForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
    PageInfo pageInfo = categoryService.listForAdmin(pageNum,pageSize);
    return ApiRestResponse.success(pageInfo);
  }


  @ApiOperation("category lisf for customer")
  @PostMapping("category/list")
  @ResponseBody
  public ApiRestResponse listCategoryForCustomer() {
    List<CategoryVO> categoryVOS = categoryService.listCategoryForCustomer(0);
    return ApiRestResponse.success(categoryVOS);
  }
}
