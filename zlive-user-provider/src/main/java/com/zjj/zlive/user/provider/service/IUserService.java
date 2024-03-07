package com.zjj.zlive.user.provider.service;

import com.zjj.zlive.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IUserService
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/5 23:37
 **/
public interface IUserService {
    String USERINFO_CACHE_PREFIX = "user-provider:userinfo:";

    /**
     * 通过id获取用户信息
     * @param userId
     * @return
     */
    UserDTO getUserById(Long userId);

    /**
     * 更新用户信息
     * @param userDTO
     * @return
     */
    Boolean updateUserById(UserDTO userDTO);

    /**
     * 添加用户
     * @param userDTO
     * @return
     */
    Boolean addUser(UserDTO userDTO);

    /**
     * 批量查询用户
     * @param userIds
     * @return
     */
    Map<Long,UserDTO> batchQueryUser(List<Long> userIds);
}
