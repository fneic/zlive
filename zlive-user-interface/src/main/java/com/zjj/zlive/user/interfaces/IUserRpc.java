package com.zjj.zlive.user.interfaces;

import com.zjj.zlive.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

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

    /**
     * 批量查询用户
     * @param userIds
     * @return
     */
    Map<Long,UserDTO> batchQueryUser(List<Long> userIds);
}
