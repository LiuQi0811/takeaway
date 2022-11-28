package com.code.takeaway.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/11/24 16:05
 * 禁用状态 0禁用 1正常
 */
@Getter
@AllArgsConstructor
public enum StatusEnum
{
    Disable(0,"禁用"),
    Normal(1,"正常");
    private Integer code;
    private String message;
}
