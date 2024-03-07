package com.zjj.zlive.api.controller;

import com.zjj.zlive.user.dto.UserDTO;
import com.zjj.zlive.user.interfaces.IUserRpc;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserController
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/5 23:55
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    @DubboReference
    private IUserRpc userRpc;

    @GetMapping("/get")
    public UserDTO getUserById(@RequestParam("id") Long id){
        return userRpc.getUserById(id);
    }

    @GetMapping("/insert")
    public void insertUser(Long userId,String nickName){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setNickName(nickName);
        userRpc.addUser(userDTO);
    }

    @GetMapping("/update")
    public void updateUser(Long userId,String nickName){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setNickName(nickName);
        userRpc.updateUserById(userDTO);
    }

    @GetMapping("/batch")
    public Map<Long,UserDTO> batchQueryUser(String userIds){
        List<Long> userList = Arrays.stream(userIds.split(",")).map(Long::parseLong).toList();
        return userRpc.batchQueryUser(userList);
    }
}
