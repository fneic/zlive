package com.zjj.zlive.api.controller;

import com.zjj.zlive.user.dto.UserDTO;
import com.zjj.zlive.user.interfaces.IUserRpc;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


}
