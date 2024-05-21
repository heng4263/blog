package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.Like;
import com.example.kkblog.domain.dto.UserDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author xiaoke
 * @Date 2024 04 13 12 15
 **/
@Repository
@Mapper
public interface LikeMapper extends BaseMapper<Like> {
    // 查询动态点赞人
    @Select("SELECT u.* FROM `user` u,likes l " +
            "where u.id=l.user_id and l.dynamic_id=#{id} " +
            "ORDER BY l.id desc LIMIT 10 ")
    List<UserDto> selectLikeUserByDynamicId(Integer id);

    // 根据用户id_动态id查询记录
    @Select("SELECT COUNT(`id`) FROM `likes` WHERE user_and_dynamic_id=#{userAndDynamicId}")
    Integer selectIfUserLike(String userAndDynamicId);

    @Delete("DELETE FROM `likes` WHERE user_and_dynamic_id=#{userAndDynamicId}")
    Integer deleteByUserDynamicId(String userAndDynamicId);
}
