package com.zjj.zlive.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * @author Zhou JunJie
 */
@Data
public class UserTagDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2070938471038894577L;
    private Long userId;
    private Long tagInfo01;

    private Long tagInfo02;

    private Long tagInfo03;
    private Date createTime;
    private Date updateTime;


}
