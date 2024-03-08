package com.zjj.zlive.user.provider.utils;

/**
 * @ClassName CacheKeyBuilder
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/8 21:07
 **/
public class CacheKeyBuilder {

    private static final String SET_TAG_LOCK_PREFIX = "zlive-user-provider:setTag:lock:";

    private static final String USER_TAG_INFO_PREFIX = "zlive-user-provider:userTag:info:";

    public static String buildSetTagLockKey(Long userId){
        return SET_TAG_LOCK_PREFIX + userId;
    }

    public static String buildTagInfoKey(Long userId){
        return USER_TAG_INFO_PREFIX + userId;
    }
}
