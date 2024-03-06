package com.zjj.zlive.user.provider.rpc;

import com.zjj.zlive.user.dto.UserDTO;
import com.zjj.zlive.user.interfaces.IUserRpc;
import com.zjj.zlive.user.provider.service.IUserService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

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
}
