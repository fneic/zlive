package com.zjj.zlive.user.provider.utils;

/**
 * @ClassName UserTagsUtils
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/8 20:04
 **/
public class UserTagsUtils {

    public static boolean isContainTag(Long tags,Long tag){
        return tags != null && tag != null && (tags&tag) > 0;
    }
}
