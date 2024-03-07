package com.zjj.zlive.idgenerate.provider.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName IdBuilderPO
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/7 20:17
 **/
@Data
@TableName("t_id_generate_config")
public class IdBuilderPO {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * id 备注描述
     */
    private String remark;
    /**
     * 初始化值
     */
    private long initNum;
    /**
     * 步长
     */
    private int step;
    /**
     * 是否是有序的 id
     */
    private int isSeq;
    /**
     * 当前 id 所在阶段的开始值
     */
    private long currentStart;
    /**
     * 当前 id 所在阶段的阈值
     */
    private long nextThreshold;
    /**
     * 业务代码前缀
     */
    private String idPrefix;
    /**
     * 乐观锁版本号
     */
    private int version;
    private Date createTime;
    private Date updateTime;
}
