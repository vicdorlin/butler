package com.butler.smartbutler.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 聚合数据物流查询结果返回
 */
@Getter
@Setter
public class JuheLogisticResultData<T> {
    private String resultcode;//200表成功
    private String reason;
    private Integer error_code;
    private T result;
}
