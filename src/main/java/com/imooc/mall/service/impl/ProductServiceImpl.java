package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.VO.CategoryVO;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.query.ProductListQuery;
import com.imooc.mall.model.request.AddProductReq;
import com.imooc.mall.model.request.ProductListReq;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired ProductMapper productMapper;

  @Autowired CategoryService categoryService;

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
    if (count == 0) {
      throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
    }
  }

  @Override
  public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {
    productMapper.batchUpdateSellStatus(ids, sellStatus);
  }

  @Override
  public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
    PageHelper.startPage(pageNum, pageSize);
    List<Product> products = productMapper.selectListForAdmin();
    PageInfo pageInfo = new PageInfo(products);
    return pageInfo;
  }

  @Override
  public Product detail(Integer id) {
    return productMapper.selectByPrimaryKey(id);
  }

  @Override
  public PageInfo list(ProductListReq productListReq) {
    //??????Query??????
    ProductListQuery productListQuery = new ProductListQuery();

    //????????????
    if (!StringUtils.isEmpty(productListReq.getKeyword())) {
      String keyword = new StringBuilder().append("%").append(productListReq.getKeyword())
              .append("%").toString();
      productListQuery.setKeyword(keyword);
    }

    //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????id???List
    if (productListReq.getCategoryId() != null) {
      List<CategoryVO> categoryVOList = categoryService
              .listCategoryForCustomer(productListReq.getCategoryId());
      ArrayList<Integer> categoryIds = new ArrayList<>();
      categoryIds.add(productListReq.getCategoryId());
      getCategoryIds(categoryVOList, categoryIds);
      productListQuery.setCategoryIds(categoryIds);
    }

    //????????????
    String orderBy = productListReq.getOrderBy();
    if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
      PageHelper
              .startPage(productListReq.getPageNum(), productListReq.getPageSize(), orderBy);
    } else {
      PageHelper
              .startPage(productListReq.getPageNum(), productListReq.getPageSize());
    }

    List<Product> productList = productMapper.selectList(productListQuery);
    PageInfo pageInfo = new PageInfo(productList);
    return pageInfo;
  }

  private void getCategoryIds(List<CategoryVO> categoryVOList, ArrayList<Integer> categoryIds) {
    for (int i = 0; i < categoryVOList.size(); i++) {
      CategoryVO categoryVO = categoryVOList.get(i);
      if (categoryVO != null) {
        categoryIds.add(categoryVO.getId());
        getCategoryIds(categoryVO.getChildCategory(), categoryIds);
      }
    }
  }
}
