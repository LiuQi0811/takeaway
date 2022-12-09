package com.code.takeaway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.code.takeaway.entity.Dish;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/9 17:30
 * 菜品 数据层
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish>
{

}
