package com.code.takeaway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.code.takeaway.entity.Category;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/2 11:43
 * 分类类目 逻辑层
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
