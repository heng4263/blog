package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Hyh
 * @Date 2024 04 11 00 09
 **/
@Repository
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    @Select("select * from `tag` where name=#{tag}")
    Tag selectTagByName(String tag);
    @Select("SELECT * FROM `tag`" +
        "WHERE `name` LIKE CONCAT('%',#{key},'%')" +
        "ORDER BY quoted_times DESC LIMIT 8")
    List<Tag> selectRecommendTags(String key);
}
