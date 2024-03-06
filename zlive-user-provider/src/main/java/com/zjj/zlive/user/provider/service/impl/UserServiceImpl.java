package com.zjj.zlive.user.provider.service.impl;

import com.zjj.zlive.common.utils.ConvertBeanUtils;
import com.zjj.zlive.user.dto.UserDTO;
import com.zjj.zlive.user.provider.dao.mapper.IUserMapper;
import com.zjj.zlive.user.provider.dao.po.UserPO;
import com.zjj.zlive.user.provider.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserServiceImpl
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/5 23:38
 **/
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;

    @Override
    public UserDTO getUserById(Long userId){
        if(userId == null){
            return null;
        }
        UserPO userPO = this.userMapper.selectById(userId);
        return ConvertBeanUtils.convert(userPO, UserDTO.class);
    }

    @Override
    public Boolean updateUserById(UserDTO userDTO) {
        if(userDTO == null || userDTO.getUserId() == null){
            return false;
        }
        return userMapper.updateById(ConvertBeanUtils.convert(userDTO,UserPO.class)) == 1;
    }

    @Override
    public Boolean addUser(UserDTO userDTO) {
        if(userDTO == null || userDTO.getUserId() == null){
            return false;
        }
        return userMapper.insert(ConvertBeanUtils.convert(userDTO,UserPO.class)) == 1;
    }
}
