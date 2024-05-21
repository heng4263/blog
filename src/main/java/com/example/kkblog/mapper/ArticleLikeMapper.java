package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.ArticleLike;
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
public interface ArticleLikeMapper extends BaseMapper<ArticleLike> {
    // 查询动态点赞人
    @Select("SELECT u.* FROM `user` u,article_like l " +
            "where u.id=l.user_id and l.article_id=#{id} " +
            "ORDER BY l.id desc LIMIT 10 ")
    List<UserDto> selectLikeUserByArticleId(Integer id);

    // 根据用户id_动态id查询记录
    @Select("SELECT COUNT(`id`) FROM `article_like` WHERE article_id=#{articleId} and user_id=#{userId}")
    Integer selectIfUserLike(ArticleLike articleLike);

    @Delete("DELETE FROM `article_like` WHERE article_id=#{articleId} and user_id=#{userId}")
    void deleteByUserLike(ArticleLike articleLike);
}
