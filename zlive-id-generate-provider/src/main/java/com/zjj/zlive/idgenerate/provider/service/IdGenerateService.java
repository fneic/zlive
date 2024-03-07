package com.zjj.zlive.idgenerate.provider.service;

/**
 * @author Zhou JunJie
 */
public interface IdGenerateService {

    /**
     * 生成有序的ID
     * @param code ID生成策略
     * @return
     */
    Long getSeqId(int code);

    /**
     * 生成无序ID
     * @param code
     * @return
     */
    Long getUnSeqId(int code);
}
