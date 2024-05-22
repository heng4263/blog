package com.example.kkblog.service;

import com.example.kkblog.domain.*;
import com.example.kkblog.domain.dto.ArticlePreDto;
import com.example.kkblog.domain.dto.DynamicPreDto;
import com.example.kkblog.domain.dto.SearchDto;
import com.example.kkblog.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author xiaoke
 * @Date 2024 04 11 00 22
 **/
@Service
public class KKBlogService {
    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private DynamicMapper dynamicMapper;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    @Autowired
    private ArticleCommentLikeMapper articleCommentLikeMapper;

    @Autowired
    private StarMapper starMapper;

    @Autowired
    private  ArticleMapper articleMapper;

    @Autowired
    private  SearchMapper searchMapper;
    // 引用标签
    public void quoteATag(String tag) {
        Tag tag1 = tagMapper.selectTagByName(tag);
        if (tag1 != null) {
            tag1.setQuotedTimes(tag1.getQuotedTimes() + 1);
            tagMapper.updateById(tag1);
        } else {
            Tag tag2 = new Tag();
            tag2.setName(tag);
            tag2.setQuotedTimes(1);
            tagMapper.insert(tag2);
        }
    }
    // 查询用户是否被另一个用户关注
    public boolean ifFollow(Integer followerId, Integer followedId) {
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowedId(followedId);
        return followMapper.selectIfFollowing(follow) > 0;
    }

    // 根据用户id查询更多的用户动态
    public List<DynamicPreDto> selectMoreDynamicsByUserId(Integer userId, Integer dynamicId) {
        Dynamic query = new Dynamic();
        query.setId(dynamicId);
        query.setUserId(userId);
        return dynamicMapper.selectMoreByUserId(query);
    }

    //根据用户id查询更多的用户文章
    public List<ArticlePreDto> selectMoreArticlesByUserId(Integer userId, Integer articleId) {
        Article query = new Article();
        query.setId(articleId);
        query.setUserId(userId);
        return articleMapper.selectMoreArticles(query);
    }

    //根据用户id查询更多的用户收藏
    public List<Star> selectMoreFavoritesByUserId(Integer userId, Integer id) {
        Star query = new Star();
        query.setId(id);
        query.setUserId(userId);
        return starMapper.selectMoreStarsByUserId(query);
    }

    //添加用户积分
    public void addUserScores(Integer userId, String title, Integer scores) {
        Score score = new Score();
        score.setScores(scores);
        score.setTitle(title);
        score.setUserId(userId);
        scoreMapper.insert(score);
        User user = new User();
        user.setId(userId);
        user.setScores(scores);
        userMapper.addUserScores(user);
    }

    // 查询更多关注用户的动态
    public List<DynamicPreDto> selectMoreFollowDynamics(Integer userId, Integer dynamicId) {
        Dynamic dynamic = new Dynamic();
        dynamic.setId(dynamicId);
        dynamic.setUserId(userId);
        return dynamicMapper.selectMoreFollowDynamics(dynamic);
    }

    // 查询用户是否点赞文章
    public boolean userLikedArticle(Integer userId, Integer articleId) {
        ArticleLike articleLike = new ArticleLike();
        articleLike.setArticleId(articleId);
        articleLike.setUserId(userId);
        return articleLikeMapper.selectIfUserLike(articleLike) > 0;
    }

    // 查询用户是否点赞文章评论
    public boolean userLikedArticleComment(Integer userId, Integer commentId) {
        ArticleCommentLike articleCommentLike = new ArticleCommentLike();
        articleCommentLike.setId(commentId);
        articleCommentLike.setUserId(userId);
        return articleCommentLikeMapper.selectIfUserLikeComment(articleCommentLike) > 0;
    }

    // 查询用户是否点赞文章评论
    public void deleteUserLikeArticleComment(Integer userId, Integer commentId) {
        ArticleCommentLike articleCommentLike = new ArticleCommentLike();
        articleCommentLike.setId(commentId);
        articleCommentLike.setUserId(userId);
        articleCommentLikeMapper.deleteByUserCommentId(articleCommentLike);
    }

    //用户搜索功能
    public List<Search> search(String keyword) {
        return searchMapper.search(keyword);
    }
}
