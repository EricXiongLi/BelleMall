package com.imooc.mall.service.impl;

import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.VO.CartVO;
import com.imooc.mall.model.dao.CartMapper;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Cart;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

  @Autowired ProductMapper productMapper;

  @Autowired CartMapper cartMapper;

  @Override
  public List<CartVO> list(Integer userId) {
    List<CartVO> cartVOS = cartMapper.selectList(userId);
    for (CartVO cartVo : cartVOS) {
      cartVo.setTotalPrice(cartVo.getPrice() * cartVo.getQuantity());
    }
    return cartVOS;
  }

  @Override
  public List<CartVO> add(Integer userId, Integer productId, Integer count)
      throws ImoocMallException {
    validProduct(productId, count);
    Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
    if (cart == null) {
      cart = new Cart();
      cart.setProductId(productId);
      cart.setUserId(userId);
      cart.setQuantity(count);
      cart.setSelected(Constant.Cart.CHECKED);
      cartMapper.insertSelective(cart);
    } else {
      count = cart.getQuantity() + count;
      Cart cartNew = new Cart();
      cartNew.setQuantity(count);
      cartNew.setId(cart.getId());
      cartNew.setUserId(cart.getUserId());
      cartNew.setProductId(cart.getProductId());
      cartNew.setSelected(Constant.Cart.CHECKED);
      cartMapper.updateByPrimaryKeySelective(cartNew);
    }
    return this.list(userId);
  }


  @Override
  public List<CartVO> update(Integer userId, Integer productId, Integer count)
          throws ImoocMallException {
    validProduct(productId, count);
    Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
    if (cart == null) {
     throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
    } else {
      Cart cartNew = new Cart();
      cartNew.setQuantity(count);
      cartNew.setId(cart.getId());
      cartNew.setUserId(cart.getUserId());
      cartNew.setProductId(cart.getProductId());
      cartNew.setSelected(Constant.Cart.CHECKED);
      cartMapper.updateByPrimaryKeySelective(cartNew);
    }
    return this.list(userId);
  }

  @Override
  public List<CartVO> delete(Integer userId, Integer productId)
          throws ImoocMallException {
    Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
    if (cart == null) {
      throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
    } else {
      cartMapper.deleteByPrimaryKey(cart.getId());
    }
    return this.list(userId);
  }

  private void validProduct(Integer productId, Integer count) throws ImoocMallException {
    Product product = productMapper.selectByPrimaryKey(productId);
    if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
      throw new ImoocMallException(ImoocMallExceptionEnum.NOT_SALE);
    }
    if (count > product.getStock()) {
      throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
    }
  }
}
