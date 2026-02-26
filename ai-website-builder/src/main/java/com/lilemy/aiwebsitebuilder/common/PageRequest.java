package com.lilemy.aiwebsitebuilder.common;

import lombok.Data;

/**
 * 分页请求
 *
 * @author lilemy
 * @date 2026-02-26 18:52
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private int pageNum = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认降序）
     */
    private String sortOrder = "descend";
}
