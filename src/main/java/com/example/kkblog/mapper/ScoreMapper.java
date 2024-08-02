package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.Score;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Hyh
 * @date 2024/4/22
 */
@Mapper
@Repository
public interface ScoreMapper extends BaseMapper<Score> {
}
