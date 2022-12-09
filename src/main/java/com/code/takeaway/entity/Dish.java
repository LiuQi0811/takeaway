package com.code.takeaway.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/9 17:18
 * 菜品
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dish implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 菜品分类id
     */
    private Long categoryId;
    /**
     * 菜品价格
     */
    private BigDecimal price;
    /**
     * 商品码
     */
    private String code;
    /**
     * 图片
     */
    private String image;
    /**
     * 描述信息
     */
    private String description;

    /**
     * 0停售 1起售
     */
    private Integer status;
    /**
     * 顺序
     */
    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
