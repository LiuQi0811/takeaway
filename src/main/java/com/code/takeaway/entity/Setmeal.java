package com.code.takeaway.entity;

import com.alibaba.druid.sql.ast.statement.SQLDropTriggerStatement;
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
 * @data 2022/12/9 17:24
 * 套餐
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Setmeal implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 套餐名称
     */
    private String name;
    /**
     * 套餐价格
     */
    private BigDecimal price;
    /**
     * 状态 0：停用 1：启用
     */
    private Integer status;
    /**
     * 编码
     */
    private String code;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 图片
     */
    private String image;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
