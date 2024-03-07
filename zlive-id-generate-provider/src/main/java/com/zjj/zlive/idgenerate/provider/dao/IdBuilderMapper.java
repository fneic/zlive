package com.zjj.zlive.idgenerate.provider.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjj.zlive.idgenerate.provider.model.po.IdBuilderPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author Zhou JunJie
 */
@Mapper
public interface IdBuilderMapper extends BaseMapper<IdBuilderPO> {

    /**
     * 全部ID生成策略
     * @return
     */
    @Select("select * from t_id_generate_config")
    List<IdBuilderPO> selectAll();

    @Update("update t_id_generate_config set current_start = next_threshold, next_threshold = next_threshold + step,version = version + 1 where id = #{id} and version = #{version}")
    int updateVersion(Integer id,Integer version);
}
