package com.imooc.mall.service.impl;

import com.imooc.mall.Utils.OrderCodeFactory;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.VO.CartVO;
import com.imooc.mall.model.dao.CartMapper;
import com.imooc.mall.model.dao.OrderItemMapper;
import com.imooc.mall.model.dao.OrderMapper;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Order;
import com.imooc.mall.model.pojo.OrderItem;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.service.CartService;
import com.imooc.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired CartService cartService;

  @Autowired ProductMapper productMapper;

  @Autowired CartMapper cartMapper;

  @Autowired OrderMapper orderMapper;

  @Autowired OrderItemMapper orderItemMapper;

  @Override
  public String create(CreateOrderReq createOrderReq) throws ImoocMallException {
    Integer userId = UserFilter.currentUser.getId();
    ArrayList<CartVO> cartVOListTemp = new ArrayList<>();
    List<CartVO> cartVOList = cartService.list(userId);
    for (CartVO cartVO : cartVOList) {
      if (cartVO.getSelected().equals(Constant.Cart.CHECKED)) {
        cartVOListTemp.add(cartVO);
      }
    }
    cartVOList = cartVOListTemp;
    if (CollectionUtils.isEmpty(cartVOList)) {
      throw new ImoocMallException(ImoocMallExceptionEnum.CART_EMPTY);
    }
    validSaleStatusAndStock(cartVOList);
    List<OrderItem> orderItemList = cartVOListToOrderItemlist(cartVOList);
    for (OrderItem orderItem : orderItemList) {
      Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
      int stock = product.getStock() - orderItem.getQuantity();
      if (stock < 0) {
        throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
      }
      product.setStock(stock);
      productMapper.updateByPrimaryKeySelective(product);
    }
    cleanCart(cartVOList);
    Order order = new Order();
    String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
    order.setOrderNo(orderNo);
    order.setUserId(userId);
    order.setTotalPrice(totalPrice(orderItemList));
    order.setReceiverName(createOrderReq.getReceiverName());
    order.setReceiverAddress(createOrderReq.getReceiverAddress());
    order.setReceiverMobile(createOrderReq.getReceiverMobile());
    order.setOrderStatus(Constant.OrderStatusEnum.NOT_PAID.getCode());
    order.setPostage(0);
    order.setPaymentType(1);
    orderMapper.insertSelective(order);
    for (OrderItem orderItem : orderItemList) {
      orderItem.setOrderNo(order.getOrderNo());
      orderItemMapper.insertSelective(orderItem);
    }
    return orderNo;
  }

  private Integer totalPrice(List<OrderItem> orderItemList) {
    Integer totalPrice = 0;
    for (OrderItem orderItem : orderItemList) {
      totalPrice += orderItem.getTotalPrice();
    }
    return totalPrice;
  }

  private void cleanCart(List<CartVO> cartVOList) {
    for (CartVO cartVO : cartVOList) {
      cartMapper.deleteByPrimaryKey(cartVO.getId());
    }
  }

  private List<OrderItem> cartVOListToOrderItemlist(List<CartVO> cartVOList) {
    List<OrderItem> orderItemList = new ArrayList<>();
    for (CartVO cartVO : cartVOList) {
      OrderItem orderItem = new OrderItem();
      orderItem.setProductId(cartVO.getProductId());
      orderItem.setProductName(cartVO.getProductName());
      orderItem.setUnitPrice(cartVO.getPrice());
      orderItem.setQuantity(cartVO.getQuantity());
      orderItem.setTotalPrice(cartVO.getTotalPrice());
      orderItemList.add(orderItem);
    }
    return orderItemList;
  }

  private void validSaleStatusAndStock(List<CartVO> cartVOList) throws ImoocMallException {
    for (CartVO cartVO : cartVOList) {
      Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
      if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
        throw new ImoocMallException(ImoocMallExceptionEnum.NOT_SALE);
      }
      if (cartVO.getQuantity() > product.getStock()) {
        throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
      }
    }
  }
}
