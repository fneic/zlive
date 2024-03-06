package com.zjj.zlive.api.controller;

import com.zjj.zlive.user.interfaces.IUserRpc;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/5 21:35
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @DubboReference
    private IUserRpc iUserRpc;


}
