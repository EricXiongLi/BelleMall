package com.imooc.mall.service;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.request.AddProductReq;

public interface ProductService {
    void add(AddProductReq addProductReq) throws ImoocMallException;
}
