package com.code.takeaway.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/5 10:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountApi {
    private String id;
    private String api;
    private Long num;
}
