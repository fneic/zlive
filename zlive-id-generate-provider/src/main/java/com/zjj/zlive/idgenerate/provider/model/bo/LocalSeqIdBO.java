package com.zjj.zlive.idgenerate.provider.model.bo;

import lombok.Builder;
import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName LocalSeqIdBO
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/7 20:20
 **/
@Data
@Builder
public class LocalSeqIdBO {

    private Integer id;


    private AtomicLong currentValue;

    private Long currentStart;

    private Long nextThreshold;

    private Integer step;


}
