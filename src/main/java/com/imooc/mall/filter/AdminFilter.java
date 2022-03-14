package com.imooc.mall.filter;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminFilter implements Filter {

  @Autowired UserService userService;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpSession httpSession = request.getSession();
    User currentUser = (User) httpSession.getAttribute(Constant.IMOOC_MALL_USER);
    if (currentUser == null) {
      PrintWriter out =
          new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
      out.write(
          "{\n"
              + "    \"status\": 10007,\n"
              + "    \"msg\": \"need login\",\n"
              + "    \"data\": null\n"
              + "}");
      out.flush();
      out.close();
      return;
    }
    boolean adminRole = userService.checkAdminRole(currentUser);
    if (adminRole) {
      filterChain.doFilter(servletRequest, servletResponse);
    } else {
      PrintWriter out =
          new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
      out.write(
          "{\n"
              + "    \"status\": 10009,\n"
              + "    \"msg\": \"need admin\",\n"
              + "    \"data\": null\n"
              + "}");
      out.flush();
      out.close();
    }
  }
}
