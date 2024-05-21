package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.Favorite;
import com.example.kkblog.domain.Like;
import com.example.kkblog.domain.Star;
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
public interface StarMapper extends BaseMapper<Star> {
    // 查询动态收藏人
    @Select("SELECT u.* FROM `user` u,star s " +
            "where u.id=s.user_id and s.dynamic_id=#{id} " +
            "ORDER BY s.id desc LIMIT 10 ")
    List<UserDto> selectStarUserByDynamicId(Integer id);

    // 查询文章收藏人
    @Select("SELECT u.* FROM `user` u,star s " +
            "where u.id=s.user_id and s.article_id=#{id} " +
            "ORDER BY s.id desc LIMIT 10 ")
    List<UserDto> selectStarUserByArticleId(Integer id);

    // 根据用户id_动态id查询记录
    @Select("SELECT COUNT(`id`) FROM `star` WHERE user_and_dynamic_id=#{userAndDynamicId}")
    Integer selectIfUserStar(String userAndDynamicId);

    // 根据用户id_文章id查询记录
    @Select("SELECT COUNT(`id`) FROM `star` WHERE user_and_article_id=#{userAndArticleId}")
    Integer selectIfUserStarArticle(String userAndArticleId);

    // 删除动态
    @Delete("DELETE FROM `star` WHERE user_and_dynamic_id=#{userAndDynamicId}")
    Integer deleteByUserDynamicId(String userAndDynamicId);

    // 删除文章
    @Delete("DELETE FROM `star` WHERE user_and_article_id=#{userAndArticleId}")
    Integer deleteByUserArticleId(String userAndArticleId);

    //查询用户收藏
    @Select("select * from `star` s " +
            "LEFT JOIN dynamic d ON s.dynamic_id = d.id " +
            "LEFT JOIN article a ON s.article_id = a.id " +
            "inner join user u on s.user_id = u.id" +
            "where s.user_id = #{userId}")
    List<Favorite> selectAllStar(Integer userId);

    //查询用户收藏
    @Select("select * from `star` s  where s.user_id=#{userId} order BY s.create_time DESC limit  5 ")
    List<Star> selectStarsByUserId(Integer userId);

    //查询用户更多收藏
    @Select("select * from `star` s where s.user_id = #{userId} and s.id < #{id} ORDER BY s.create_time DESC LIMIT 3")
    List<Star> selectMoreStarsByUserId(Star star);
}
