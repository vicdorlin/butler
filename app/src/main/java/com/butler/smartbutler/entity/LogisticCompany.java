package com.butler.smartbutler.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 物流企业
 */
@Getter
@Setter
public class LogisticCompany {
    private String com;//公司名字
    private String no;//公司编号

    public LogisticCompany() {
    }

    public LogisticCompany(String com, String no) {
        this.com = com;
        this.no = no;
    }
}
