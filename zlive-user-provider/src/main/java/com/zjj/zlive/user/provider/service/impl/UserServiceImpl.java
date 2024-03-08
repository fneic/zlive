package com.zjj.zlive.user.provider.service.impl;

import com.alibaba.fastjson2.JSON;
import com.zjj.zlive.common.utils.ConvertBeanUtils;
import com.zjj.zlive.user.dto.UserDTO;
import com.zjj.zlive.user.provider.common.MqSender;
import com.zjj.zlive.user.provider.dao.mapper.IUserMapper;
import com.zjj.zlive.user.provider.dao.po.UserPO;
import com.zjj.zlive.user.provider.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName UserServiceImpl
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/5 23:38
 **/
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;
    
    @Resource
    private RedisTemplate<String,UserDTO> redisTemplate;

    @Resource
    private MqSender mqSender;

//    private final String USERINFO_CACHE_PREFIX = "user-provider:userinfo:";

    @Override
    public UserDTO getUserById(Long userId){
        if(userId == null){
            return null;
        }
        String key = USERINFO_CACHE_PREFIX + userId;
        UserDTO userDTO = redisTemplate.opsForValue().get(key);
        if(userDTO != null){
            return userDTO;
        }
        userDTO = ConvertBeanUtils.convert(userMapper.selectById(userId),UserDTO.class);
        if(userDTO != null){
            redisTemplate.opsForValue().set(key,userDTO,30,TimeUnit.MINUTES);
        }
        return userDTO;
    }

    @Override
    public Boolean updateUserById(UserDTO userDTO) {
        if(userDTO == null || userDTO.getUserId() == null){
            return false;
        }
        int effect = userMapper.updateById(ConvertBeanUtils.convert(userDTO, UserPO.class));
        if(effect == 0){
            return false;
        }
        String key = USERINFO_CACHE_PREFIX + userDTO.getUserId();
        redisTemplate.delete(key);
        //延迟双删
        mqSender.sendCacheDeleteMessage(key);
        return true;
    }

    @Override
    public Boolean addUser(UserDTO userDTO) {
        if(userDTO == null || userDTO.getUserId() == null){
            return false;
        }
        return userMapper.insert(ConvertBeanUtils.convert(userDTO,UserPO.class)) == 1;
    }

    @Override
    public Map<Long, UserDTO> batchQueryUser(List<Long> userIds) {
        if(userIds == null || userIds.isEmpty()){
            return new HashedMap<>();
        }
        List<Long> checkIds = userIds.stream().filter(userId -> userId > 1000).toList();
        if(checkIds.isEmpty()){
            return new HashedMap<>();
        }
        List<String> keys = new ArrayList<>();
        //从redis中先做查询
        checkIds.forEach(userId -> keys.add(USERINFO_CACHE_PREFIX + userId));
        List<UserDTO> cacheUsers = redisTemplate.opsForValue().multiGet(keys);
        assert cacheUsers != null;
        cacheUsers = cacheUsers.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if(cacheUsers.size() == checkIds.size()){
            return cacheUsers.stream().collect(Collectors.toMap(UserDTO::getUserId, userDTO -> userDTO));
        }
        //redis中没有的部分再从db中查
        List<Long> inCacheIds = cacheUsers.stream().map(UserDTO::getUserId).toList();
        List<Long> notInCacheIds = checkIds.stream().filter(id -> !inCacheIds.contains(id)).toList();
        Map<Long, List<Long>> idsByDB = notInCacheIds.stream().collect(Collectors.groupingBy(userId -> userId % 100));
        List<UserDTO> usersInDB = new CopyOnWriteArrayList<>();
        idsByDB.values().parallelStream().forEach(ids -> usersInDB.addAll(ConvertBeanUtils.convertList(userMapper.selectBatchIds(ids),UserDTO.class)));
        //查询的数据在存入redis中
        if(!usersInDB.isEmpty()){
            Map<String, UserDTO> toCacheUsers = usersInDB.stream().collect(Collectors.toMap(userDTO -> USERINFO_CACHE_PREFIX + userDTO.getUserId(), x -> x));
            redisTemplate.opsForValue().multiSet(toCacheUsers);
            //设置过期时间，加上随机过期时间，防止发生缓存雪崩
            redisTemplate.executePipelined(new SessionCallback<Object>() {
                @Override
                public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                    for (String key : toCacheUsers.keySet()) {
                        operations.expire((K)key,createRandomExpireTime(30*60), TimeUnit.SECONDS);
                    }
                    return null;
                }
            });
            cacheUsers.addAll(usersInDB);
        }
        return cacheUsers.stream().collect(Collectors.toMap(UserDTO::getUserId,x->x));
    }

    private int createRandomExpireTime(int time){
        int randomSeconds = ThreadLocalRandom.current().nextInt(1000);
        return randomSeconds + time;
    }
}
