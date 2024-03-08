package com.zjj.zlive.user.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjj.zlive.user.provider.dao.po.UserTagPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author Zhou JunJie
 */
@Mapper
public interface IUserTagMapper extends BaseMapper<UserTagPO> {
    /**
     *
     * 设置用户标签
     * @param userId 用户ID
     * @param tag 标签值
     * @param fieldName 字段名
     * @return
     */
    @Update("update t_user_tag set ${fieldName} = ${fieldName} | #{tag} where user_id = #{userId} and ${fieldName} & #{tag} = 0")
    int setTag(Long userId, long tag, String fieldName);

    /**
     * 删除用户标签
     * @param userId
     * @param tag
     * @param fieldName
     * @return
     */
    @Update("update t_user_tag set ${fieldName} = ${fieldName} &~ #{tag} where user_id = #{userId} and ${fieldName} & #{tag} = #{tag}")
    int deleteTag(Long userId, long tag, String fieldName);

    /**
     * 查询用户标签
     * @param userId
     * @return
     */
    @Select("select * from t_user_tag where user_id = #{userId}")
    UserTagPO getUserTag(Long userId);

}
