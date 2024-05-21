package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.VerCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author xiaoke
 * @Date 2024 04 17 23 19
 **/
@Mapper
@Repository
public interface VerCodeMapper extends BaseMapper<VerCode> {
    @Select("SELECT * FROM `ver_code` WHERE `code_key`=#{codeKey}")
    VerCode selectCodeByKey(String key);

}
