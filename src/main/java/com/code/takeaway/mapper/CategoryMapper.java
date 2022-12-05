package com.code.takeaway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.code.takeaway.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/2 11:42
 * 分类类目 数据层
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
