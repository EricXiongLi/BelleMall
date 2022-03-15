package com.imooc.mall.service;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.AddProductReq;

public interface ProductService {
    void add(AddProductReq addProductReq) throws ImoocMallException;

    void update(Product updatedProduct) throws ImoocMallException;

    void delete(Integer id) throws ImoocMallException;

    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);
}
