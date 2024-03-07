package com.zjj.zlive.user.provider.rpc;

import com.zjj.zlive.user.dto.UserDTO;
import com.zjj.zlive.user.interfaces.IUserRpc;
import com.zjj.zlive.user.provider.service.IUserService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;
import java.util.Map;

/**
 * @ClassName UserRpcImpl
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/5 20:41
 **/
@DubboService
public class UserRpcImpl implements IUserRpc {

    @Resource
    private IUserService userService;

    @Override
    public UserDTO getUserById(Long userId) {
        return userService.getUserById(userId);
    }

    @Override
    public Boolean updateUserById(UserDTO userDTO) {
        return userService.updateUserById(userDTO);
    }

    @Override
    public Boolean addUser(UserDTO userDTO) {
        return userService.addUser(userDTO);
    }

    @Override
    public Map<Long, UserDTO> batchQueryUser(List<Long> userIds) {
        return userService.batchQueryUser(userIds);
    }
}
