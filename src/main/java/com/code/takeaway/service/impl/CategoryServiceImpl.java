package com.code.takeaway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.code.takeaway.entity.Category;
import com.code.takeaway.mapper.CategoryMapper;
import com.code.takeaway.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/2 11:44
 * 分类类目 逻辑实现层
 */
@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService
{

    @Override
    public void remove(Long id) {

    }
}
