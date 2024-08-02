package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.Image;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Hyh
 * @Date 2024 04 10 22 00
 **/
@Repository
@Mapper
public interface ImageMapper extends BaseMapper<Image> {
}
