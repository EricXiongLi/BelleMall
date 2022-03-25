package com.imooc.mall.common;

import com.google.common.collect.Sets;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Constant {
  public static final String IMOOC_MALL_USER = "mall_user";
  public static final String SALT = "1ghedsghk,];dfs..,./[]";

  public static String FILE_UPLOAD_DIR;

  @Value("${file.upload.dir}")
  public void setFileUploadDir(String fileUploadDir) {
    FILE_UPLOAD_DIR = fileUploadDir;
  }

  public interface ProductListOrderBy {
    Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc", "price asc");
  }

  public interface SaleStatus {
    int NOT_SALE = 0;
    int SALE = 1;
  }

  public interface Cart {
    int UN_CHECKED = 0;
    int CHECKED = 1;
  }

  public enum OrderStatusEnum {
    CANCELLED("order cancelled", 0),
    NOT_PAID("not paid", 10),
    PAID("paid", 20),
    DELIVERED("delivered", 30),
    FINISHED("completed", 40);

    private String name;
    private int code;

    OrderStatusEnum(String name, int code) {
      this.name = name;
      this.code = code;
    }

    public static OrderStatusEnum codeOf(int code) throws ImoocMallException {
      for (OrderStatusEnum orderStatusEnum : values()) {
        if (orderStatusEnum.getCode() == code) {
          return orderStatusEnum;
        }
      }
      throw new ImoocMallException(ImoocMallExceptionEnum.NO_ENUM);
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }
  }
}
