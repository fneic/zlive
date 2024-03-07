package com.zjj.zlive.idgenerate.provider.service.impl;

import com.zjj.zlive.idgenerate.provider.dao.IdBuilderMapper;
import com.zjj.zlive.idgenerate.provider.model.bo.LocalSeqIdBO;
import com.zjj.zlive.idgenerate.provider.model.bo.LocalUnSeqIdBO;
import com.zjj.zlive.idgenerate.provider.model.po.IdBuilderPO;
import com.zjj.zlive.idgenerate.provider.service.IdGenerateService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName IdGenerateServiceImpl
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/7 20:15
 **/
@Slf4j
@Service
public class IdGenerateServiceImpl implements IdGenerateService , InitializingBean {

    Map<Integer, LocalSeqIdBO> localSeqIdMap = new ConcurrentHashMap<>();

    Map<Integer, LocalUnSeqIdBO> localUnSeqIdMap = new ConcurrentHashMap<>();

    Map<Integer, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    @Resource
    private IdBuilderMapper idBuilderMapper;

    private static final float LOAD_FACTOR = 0.75f;

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 20, 10, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1000), (task) -> {
        Thread thread = new Thread(task);
        thread.setName("[seq id generator update::");
        return thread;
    });

    @Override
    public Long getSeqId(int code) {
        LocalSeqIdBO localSeqIdBO = localSeqIdMap.get(code);
        if(localSeqIdBO == null){
            log.error("[getSeqId] code is not exist,code is {}",code);
            return null;
        }
        Long retId = localSeqIdBO.getCurrentValue().getAndIncrement();
        if(retId >= localSeqIdBO.getNextThreshold()){
            log.error("[getSeqId] id is over limit, id is {},code is {}",retId,code);
            return null;
        }
        trySeqRefresh(localSeqIdBO);
        return retId;
    }

    @Override
    public Long getUnSeqId(int code) {
        LocalUnSeqIdBO localUnSeqIdBO = localUnSeqIdMap.get(code);
        if(localUnSeqIdBO == null){
            log.error("[getUnSeqId] code is not exist,code is {}",code);
            return null;
        }
        Long retId = localUnSeqIdBO.getUnSeqId();
        tryUnSeqRefresh(localUnSeqIdBO);
        return retId;

    }

    private void trySeqRefresh(LocalSeqIdBO localSeqIdBO){
        //超过阈值，提前更新
        if(localSeqIdBO.getCurrentValue().get() - localSeqIdBO.getCurrentStart() > LOAD_FACTOR * localSeqIdBO.getStep()){
            log.info("超过阈值，当前值：{}，起始值：{}，阈值：{}",localSeqIdBO.getCurrentValue().get(),localSeqIdBO.getCurrentStart(),LOAD_FACTOR * localSeqIdBO.getStep());
            refreshSeqId(localSeqIdBO.getId());
        }
    }

    private void tryUnSeqRefresh(LocalUnSeqIdBO localUnSeqIdBO){
        //超过阈值，提前更新
        if(localUnSeqIdBO.getSize() < (1 - LOAD_FACTOR) * localUnSeqIdBO.getStep()){
            refreshSeqId(localUnSeqIdBO.getId());
        }
    }

    /**
     * 获取新的ID分片
     * @param id
     */
    private void refreshSeqId(Integer id) {
        Semaphore semaphore = semaphoreMap.get(id);
        boolean acquire = semaphore.tryAcquire();
        if(acquire){
            threadPoolExecutor.execute(() -> {
                for(int i = 0; i < 3; i++){
                    log.info("尝试第 {} 次异步更新刷新ID，code is {}",i+1,id);
                    IdBuilderPO idBuilderPO = idBuilderMapper.selectById(id);
                    int updateStatus = idBuilderMapper.updateVersion(idBuilderPO.getId(), idBuilderPO.getVersion());
                    if(updateStatus > 0){
                        if(idBuilderPO.getIsSeq() == 0){
                            LocalSeqIdBO localSeqIdBO = localSeqIdMap.get(id);
                            localSeqIdBO.setNextThreshold(idBuilderPO.getNextThreshold());
                            localSeqIdBO.setStep(idBuilderPO.getStep());
                            AtomicLong currentValue = new AtomicLong(idBuilderPO.getCurrentStart());
                            //todo 更新后，会损失部分的id，可以改善
                            localSeqIdBO.setCurrentValue(currentValue);
                            localSeqIdBO.setCurrentStart(idBuilderPO.getCurrentStart());
                        }
                        else{
                            LocalUnSeqIdBO localUnSeqIdBO = localUnSeqIdMap.get(id);
                            localUnSeqIdBO.setStep(idBuilderPO.getStep());
                            long currentStart = idBuilderPO.getCurrentStart();
                            long nextThreshold = idBuilderPO.getNextThreshold();
                            localUnSeqIdBO.setCurrentStart(currentStart);
                            localUnSeqIdBO.setNextThreshold(nextThreshold);
                            localUnSeqIdBO.setRandomIdInQue(currentStart,nextThreshold);
                        }
                        log.info("第 {} 次异步更新刷新ID成功，code is {}",i+1,id);
                        break;
                    }
                }
                semaphore.release();
            });
        }
    }

    /**
     * 初始化从数据库读取ID分片
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<IdBuilderPO> idBuilderPOS = idBuilderMapper.selectAll();
        for (IdBuilderPO idBuilderPO : idBuilderPOS) {
            for(int i = 0; i < 3; i++){
                int updateStatus = idBuilderMapper.updateVersion(idBuilderPO.getId(), idBuilderPO.getVersion());
                if(updateStatus > 0){
                    //有序id容器
                    if(idBuilderPO.getIsSeq() == 0){
                        AtomicLong currentValue = new AtomicLong(idBuilderPO.getCurrentStart());
                        LocalSeqIdBO localSeqIdBO = LocalSeqIdBO.builder().id(idBuilderPO.getId()).step(idBuilderPO.getStep())
                                .currentValue(currentValue).currentStart(idBuilderPO.getCurrentStart())
                                .nextThreshold(idBuilderPO.getNextThreshold()).build();
                        localSeqIdMap.put(idBuilderPO.getId(), localSeqIdBO);
                    }
                    else{
                        LocalUnSeqIdBO localUnSeqIdBO = LocalUnSeqIdBO.builder().id(idBuilderPO.getId())
                                .currentStart(idBuilderPO.getCurrentStart())
                                .step(idBuilderPO.getStep())
                                .nextThreshold(idBuilderPO.getNextThreshold())
                                .build();
                        localUnSeqIdBO.setRandomIdInQue(idBuilderPO.getCurrentStart(),idBuilderPO.getNextThreshold());
                        localUnSeqIdMap.put(idBuilderPO.getId(),localUnSeqIdBO);
                    }
                    Semaphore semaphore = new Semaphore(1);
                    semaphoreMap.put(idBuilderPO.getId(),semaphore);
                    break;
                }else{
                    idBuilderPO = idBuilderMapper.selectById(idBuilderPO.getId());
                }
            }
        }
    }
}
