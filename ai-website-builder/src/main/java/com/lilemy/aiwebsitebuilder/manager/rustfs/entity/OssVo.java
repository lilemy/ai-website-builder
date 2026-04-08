package com.lilemy.aiwebsitebuilder.manager.rustfs.entity;

import lombok.Data;

/**
 * 对象存储信息
 *
 * @author lilemy
 * @date 2026-04-08 23:03
 */
@Data
public class OssVo {

    /**
     * 对象存储地址
     */
    private String url;

    /**
     * 对象存储大小
     */
    private Long size;
}
