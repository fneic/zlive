package com.zjj.zlive.user.interfaces;

import com.zjj.zlive.user.dto.UserDTO;

/**
 * @author Zhou JunJie
 */
public interface IUserRpc {

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
