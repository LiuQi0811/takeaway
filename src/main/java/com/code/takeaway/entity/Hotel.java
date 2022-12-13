package com.code.takeaway.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/13 10:45
 * 酒店实体类
 */
@Data
@TableName("tb_hotel")
public class Hotel implements Serializable {

    /**
     * 酒店id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 酒店名称
     */
    @TableField("name")
    private String name;

    /**
     * 酒店地址
     */
    @TableField("address")
    private String address;

    /**
     * 酒店价格
     */
    @TableField("price")
    private Integer price;

    /**
     * 酒店评分
     */
    @TableField("score")
    private Integer score;

    /**
     * 酒店品牌
     */
    @TableField("brand")
    private String brand;

    /**
     * 所在城市
     */
    @TableField("city")
    private String city;

    /**
     * 酒店星级，1星到5星，1钻到5钻
     */
    @TableField("star_name")
    private String starName;

    /**
     * 商圈
     */
    @TableField("business")
    private String business;

    /**
     * 纬度
     */
    @TableField("latitude")
    private String latitude;

    /**
     * 经度
     */
    @TableField("longitude")
    private String longitude;

    /**
     * 酒店图片
     */
    @TableField("pic")
    private String pic;
}

