package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.AddProductReq;
import com.imooc.mall.model.request.UpdateProductReq;
import com.imooc.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
public class ProductAdminController {

  @Autowired ProductService productService;

  @ApiOperation("add new product")
  @PostMapping("admin/product/add")
  public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq)
      throws ImoocMallException {
    productService.add(addProductReq);
    return ApiRestResponse.success();
  }

  @ApiOperation("upload file for admin")
  @PostMapping("/admin/upload/file")
  public ApiRestResponse upload(
      HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file)
      throws ImoocMallException {

    String fileName = file.getOriginalFilename();
    String suffixName = fileName.substring(fileName.lastIndexOf("."));
    // generate UUID
    UUID uuid = UUID.randomUUID();
    String newFileName = uuid.toString() + suffixName;
    File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
    File destFIle = new File(Constant.FILE_UPLOAD_DIR + newFileName);
    if (!fileDirectory.exists()) {
      if (!fileDirectory.mkdirs()) {
        throw new ImoocMallException(ImoocMallExceptionEnum.MKDIR_FAILED);
      }
    }
    try {
      file.transferTo(destFIle);
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      return ApiRestResponse.success(
          getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/images/" + newFileName);
    } catch (URISyntaxException e) {
      throw new ImoocMallException(ImoocMallExceptionEnum.UPLOAD_FAILED);
    }
  }

  public URI getHost(URI uri) {
    URI effectiveUri;
    try {
      effectiveUri =
          new URI(
              uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
    } catch (URISyntaxException e) {
      effectiveUri = null;
    }
    return effectiveUri;
  }

  @ApiOperation("update product")
  @PostMapping("admin/product/update")
  public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq) throws ImoocMallException {
    Product product = new Product();
    BeanUtils.copyProperties(updateProductReq,product);
    productService.update(product);
    return ApiRestResponse.success();
  }

  @ApiOperation("delete product")
  @PostMapping("admin/product/delete")
  public ApiRestResponse deleteProduct(@RequestParam Integer id) throws ImoocMallException {
    productService.delete(id);
    return ApiRestResponse.success();
  }

  @ApiOperation("batch update the status of product")
  @PostMapping("/admin/product/batchUpdateSellStatus")
  public ApiRestResponse batchUpdateSellStatus(@RequestParam Integer[] ids,@RequestParam Integer sellStatus) {
    productService.batchUpdateSellStatus(ids, sellStatus);
    return ApiRestResponse.success();
  }

}
