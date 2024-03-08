package com.zjj.zlive.user.provider.service.impl;

import com.zjj.zlive.user.consistant.UserTagsEnum;
import com.zjj.zlive.user.provider.dao.mapper.IUserTagMapper;
import com.zjj.zlive.user.provider.dao.po.UserTagPO;
import com.zjj.zlive.user.provider.service.IUserTagService;
import com.zjj.zlive.user.provider.utils.CacheKeyBuilder;
import com.zjj.zlive.user.provider.utils.UserTagsUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @ClassName UserTagService
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/8 19:38
 **/
@Service
@Slf4j
public class UserTagService implements IUserTagService {
    @Resource
    private IUserTagMapper userTagMapper;

    @Resource
    private RedissonClient redissonClient;
    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        if(userTagMapper.setTag(userId, userTagsEnum.getTag(), userTagsEnum.getFieldName()) > 0){
            return true;
        }
        RLock lock = redissonClient.getLock(CacheKeyBuilder.buildSetTagLockKey(userId));
        boolean acquire = lock.tryLock();
        if (acquire){
            try{
                System.out.println("抢占到锁，执行插入");
                //设置失败：1.标签已存在 2.还没有记录
                UserTagPO userTagPO = userTagMapper.selectById(userId);
                if(userTagPO != null){
                    return false;
                }
                userTagPO = new UserTagPO();
                userTagPO.setUserId(userId);
                userTagMapper.insert(userTagPO);
                return userTagMapper.setTag(userId, userTagsEnum.getTag(), userTagsEnum.getFieldName()) > 0;
            }catch (Exception e){
                log.error(Arrays.toString(e.getStackTrace()));
            }finally {
                lock.unlock();
            }
        }
        return false;
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagMapper.deleteTag(userId,userTagsEnum.getTag(), userTagsEnum.getFieldName()) > 0;
    }

    @Override
    public boolean containsTag(Long userId, UserTagsEnum userTagsEnum) {
        UserTagPO userTag = userTagMapper.getUserTag(userId);
        String fieldName = userTagsEnum.getFieldName();
        boolean res = false;
        if("tag_info_01".equals(fieldName)){
            res = UserTagsUtils.isContainTag(userTag.getTagInfo01(),userTagsEnum.getTag());
        }
        else if ("tag_info_02".equals(fieldName)){
            res = UserTagsUtils.isContainTag(userTag.getTagInfo02(),userTagsEnum.getTag());
        }
        else{
            res = UserTagsUtils.isContainTag(userTag.getTagInfo03(),userTagsEnum.getTag());
        }
        return res;
    }
}
