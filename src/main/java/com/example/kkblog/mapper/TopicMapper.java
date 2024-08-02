package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.Topic;
import com.example.kkblog.domain.dto.TopicDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Hyh
 * @date 2024/4/7
 */
@Mapper
@Repository
public interface TopicMapper extends BaseMapper<Topic> {
    @Select("SELECT * FROM `topic` WHERE status=0 ORDER BY 'rank' asc")
    List<TopicDto> selectAvailableTopics();



}
