package com.butler.smartbutler.entity;


import cn.bmob.v3.BmobUser;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User extends BmobUser {
    private Integer age;
    private Integer sex;
    private String desc;
}
