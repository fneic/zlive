package com.zjj.zlive.user.provider.service;

import com.zjj.zlive.user.consistant.UserTagsEnum;

/**
 * @author Zhou JunJie
 */
public interface IUserTagService {

    /**
     * 设置用户标签
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean setTag(Long userId, UserTagsEnum userTagsEnum);

    /**
     * 取消用户标签
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean cancelTag(Long userId,UserTagsEnum userTagsEnum);

    /**
     * 查询用户是否包含该标签
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean containsTag(Long userId,UserTagsEnum userTagsEnum);
}
