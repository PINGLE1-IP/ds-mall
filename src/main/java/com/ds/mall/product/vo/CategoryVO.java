package com.ds.mall.product.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryVO {

    private Long id;
    private Long parentId;
    private String name;
    private String path;
    private Integer level;
    private Integer sortOrder;
    private Integer enabled;
    private String iconUrl;
    private List<CategoryVO> children = new ArrayList<>();
}
