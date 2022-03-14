package com.imooc.mall.common;

import org.springframework.beans.factory.annotation.Value;

public class Constant {
    public static final String IMOOC_MALL_USER = "mall_user";
    public static final String SALT = "1ghedsghk,];dfs..,./[]";

    @Value("${file.upload.dir}")
    public static String FILE_UPLOAD_DIR;
}
