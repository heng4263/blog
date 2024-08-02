package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.SystemConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Hyh
 * @date 2024/4/7
 */
@Mapper
@Repository
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {
}
