package com.zjj.zlive.user.provider.service;

import com.zjj.zlive.user.dto.UserDTO;

/**
 * @ClassName IUserService
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/5 23:37
 **/
public interface IUserService {

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
}
