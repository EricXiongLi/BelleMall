package com.imooc.mall.service.impl;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.AddProductReq;
import com.imooc.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired ProductMapper productMapper;

  @Override
  public void add(AddProductReq addProductReq) throws ImoocMallException {
    Product product = new Product();
    BeanUtils.copyProperties(addProductReq, product);
    Product productOld = productMapper.selectByName(addProductReq.getName());
    if (productOld != null) {
      throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
    }
    int count = productMapper.insertSelective(product);
    if (count == 0) {
      throw new ImoocMallException(ImoocMallExceptionEnum.CREATE_FAILED);
    }
  }

  @Override
  public void update(Product updatedProduct) throws ImoocMallException {
    Product productOld = productMapper.selectByName(updatedProduct.getName());
    if (productOld != null && productOld.getId().equals(updatedProduct.getId())) {
      throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
    }
    int count = productMapper.updateByPrimaryKeySelective(updatedProduct);
    if (count == 0) {
      throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
    }
  }

  @Override
  public void delete(Integer id) throws ImoocMallException {
    Product productOld = productMapper.selectByPrimaryKey(id);
    if (productOld == null) {
      throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
    }
    int count = productMapper.deleteByPrimaryKey(id);
    if (count==0) {
      throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
    }
  }

  @Override
  public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {
    productMapper.batchUpdateSellStatus(ids, sellStatus);
  }
}
