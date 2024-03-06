package com.zjj.zlive.user.dto;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName UserDto
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/5 23:40
 **/
@Data
public class UserDTO {
    private Long userId;
    private String nickName;
    private String trueName;
    private String avatar;
    private Integer sex;
    private Integer workCity;
    private Integer bornCity;
    private Date bornDate;
    private Date createTime;
    private Date updateTime;
}
