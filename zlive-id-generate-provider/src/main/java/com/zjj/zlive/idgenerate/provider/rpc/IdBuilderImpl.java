package com.zjj.zlive.idgenerate.provider.rpc;

import com.zjj.zlive.idgenerate.provider.service.IdGenerateService;
import com.zlive.idgenerate.interfaces.IdBuilderRpc;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @ClassName IdBuilderImpl
 * @Description 提供分布式ID生成方式实现
 * @Author Zhou JunJie
 * @Date 2024/3/7 20:12
 **/
@DubboService
public class IdBuilderImpl implements IdBuilderRpc {

    @Resource
    private IdGenerateService idGenerateService;

    @Override
    public Long increaseSeqId(int code) {
        return idGenerateService.getSeqId(code);
    }

    @Override
    public Long increaseUnSeqId(int code) {
       return idGenerateService.getUnSeqId(code);
    }

    @Override
    public String increaseSeqStrId(int code) {
        Long seqId = idGenerateService.getSeqId(code);
        if(seqId == null){
            return null;
        }
        return seqId.toString();
    }
}
