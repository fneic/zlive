package com.zjj.zlive.user.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjj.zlive.user.provider.dao.po.UserPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName UserMapper
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/5 23:35
 **/
@Mapper
public interface IUserMapper extends BaseMapper<UserPO> {
}
