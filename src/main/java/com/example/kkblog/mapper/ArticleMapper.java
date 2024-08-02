package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.Article;
import com.example.kkblog.domain.dto.ArticleDetailDto;
import com.example.kkblog.domain.dto.ArticlePreDto;
import com.example.kkblog.domain.dto.FavoriteDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Hyh
 * @date 2024/4/29
 */
@Mapper
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    // 根据用户id获取文章
    @Select("SELECT w.*,COUNT(c.id) as commentNumber FROM   " +
            "(SELECT a.*, u.nickname as nickname, u.avatar as avatar FROM `article` a,`user` u   " +
            "WHERE  a.user_id=u.id and a.status=0 and a.user_id=#{id} ORDER BY a.id DESC LIMIT 5) as w  " +
            "LEFT JOIN article_comment c  " +
            "ON w.id = c.article_id  " +
            "GROUP BY w.id " +
            "ORDER BY w.id DESC")
    List<ArticlePreDto> selectByUserId(Integer id);

    //根据用户id删除文章
    @Select("SELECT a.*,COUNT(c.id) as commentNumber FROM  " +
            "(SELECT d.*, u.nickname as nickname, u.avatar as avatar FROM `article` d, `user` u  " +
            "WHERE d.user_id=u.id and d.status=0 and d.id=#{id}) as a " +
            "LEFT JOIN article_comment c " +
            "ON a.id = c.article_id " +
            "GROUP BY a.id")
    ArticleDetailDto selectDetailById(String id);

    //文章浏览量
    @Update("UPDATE `article` SET `view_number`=`view_number` + 1 WHERE id=#{id}")
    void addViewById(String id);

    //文章点赞
    @Update("UPDATE `article` SET `like_number`=`like_number` + 1 WHERE id=#{id}")
    void addLikeById(Integer id);

    //文章取消点赞
    @Update("UPDATE `article` SET `like_number`=`like_number` - 1 WHERE id=#{id}")
    void cancelLikeById(Integer id);

    // 删除文章
    @Update("UPDATE `article` SET status=2 WHERE id=#{id}")
    Integer deleteArticleById(Integer id);

    //获取最新文章
    @Select("SELECT a.*,COUNT(c.id) as commentNumber FROM   " +
            "(SELECT d.*,u.nickname as nickname, u.avatar as avatar FROM `article` d, `user` u   " +
            "WHERE d.user_id=u.id and d.status=0 ORDER BY d.id DESC LIMIT 8) as a  " +
            "LEFT JOIN article_comment c  " +
            "ON a.id = c.article_id  " +
            "GROUP BY a.id " +
            "ORDER BY a.id DESC")
    List<ArticlePreDto> selectNewestArticles();
    //获取更多最新文章
    @Select("SELECT a.*,COUNT(c.id) as commentNumber FROM   " +
            "(SELECT d.*, u.nickname as nickname, u.avatar as avatar FROM `article` d,`user` u   " +
            "WHERE d.user_id=u.id and d.status=0  and d.id < #{id} ORDER BY d.id DESC LIMIT 8) as a  " +
            "LEFT JOIN article_comment c  " +
            "ON a.id = c.article_id  " +
            "GROUP BY a.id " +
            "ORDER BY a.id DESC")
    List<ArticlePreDto> selectMoreNewestArticles(Integer id);

    // 根据用户id获取更多文章
    @Select("SELECT a.*,COUNT(c.id) as commentNumber FROM   " +
            "(SELECT d.*, u.nickname as nickname, u.avatar as avatar FROM `article` d,`user` u   " +
            "WHERE d.user_id=u.id and d.status=0 and d.user_id=#{userId} and d.id < #{id} ORDER BY d.id DESC LIMIT 8) as a  " +
            "LEFT JOIN article_comment c  " +
            "ON a.id = c.article_id  " +
            "GROUP BY a.id " +
            "ORDER BY a.id DESC")
    List<ArticlePreDto> selectMoreArticles(Article article);

    //文章收藏
    @Update("UPDATE `article` SET `star_number`=`star_number` + 1 WHERE id=#{id}")
    void  addStarById(Integer id);

    //取消收藏文章
    @Update("UPDATE `article` SET `star_number`=`star_number` - 1 WHERE id=#{id}")
    void  cancelStarById(Integer id);

}
