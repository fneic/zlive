package com.zjj.zlive.idgenerate.provider.model.bo;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName LocalSeqIdBO
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/7 20:20
 **/
@Data
@Builder
@Slf4j
public class LocalUnSeqIdBO {

    private Integer id;

    private ConcurrentLinkedQueue<Long> idQue;

    private Long currentStart;

    private Long nextThreshold;

    private Integer step;

    public void setRandomIdInQue(long start,long nextThreshold){
        List<Long> list = new ArrayList<>();
        for (long i = start; i < nextThreshold; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        idQue = new ConcurrentLinkedQueue<>(list);
    }

    public Long getUnSeqId(){
        if(idQue.isEmpty()){
            log.error("[getSeqId] id is over limit, id is {},code is {}",id);
            return null;
        }
        return idQue.poll();
    }

    public int getSize(){
        return idQue.size();
    }
}
