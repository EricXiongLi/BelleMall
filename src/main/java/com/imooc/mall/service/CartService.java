package com.imooc.mall.service;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.VO.CartVO;

import java.util.List;

public interface CartService {

    List<CartVO> list(Integer userId);

    List<CartVO> add(Integer userId, Integer productId, Integer count) throws ImoocMallException;

    List<CartVO> update(Integer userId, Integer productId, Integer count)
            throws ImoocMallException;

    List<CartVO> delete(Integer userId, Integer productId)
            throws ImoocMallException;


    List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selected) throws ImoocMallException;

    List<CartVO> selectAllOrNot(Integer userId, Integer selected);
}
