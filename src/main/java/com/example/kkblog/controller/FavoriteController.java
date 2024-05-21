package com.example.kkblog.controller;

import com.example.kkblog.controller.dto.ResponseDto;
import com.example.kkblog.domain.Article;
import com.example.kkblog.domain.Dynamic;
import com.example.kkblog.domain.Favorite;
import com.example.kkblog.domain.Star;
import com.example.kkblog.domain.dto.ArticlePreDto;
import com.example.kkblog.domain.dto.DynamicPreDto;
import com.example.kkblog.domain.dto.FavoriteDto;
import com.example.kkblog.domain.dto.UserDto;
import com.example.kkblog.mapper.*;
import com.example.kkblog.service.KKBlogService;
import com.example.kkblog.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author xiaoke
 * @Date 2024 04 11 00 11
 **/
@Controller
@RequestMapping("/user")
public class FavoriteController {


    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private StarMapper starMapper;

    @Autowired
    private DynamicMapper dynamicMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private DynamicCommentMapper dynamicCommentMapper;

    @Autowired
    private ArticleCommentMapper articleCommentMapper;

    @Autowired
    private KKBlogService kkBlogService;

    @Autowired
    private LikeMapper likeMapper;


    @GetMapping("/favorites")
    public String me(HttpSession session, Model model) {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        return favorites(loginUser.getId(), session, model);
    }

    @GetMapping("/favorites/{id}")
    public String favorites(@PathVariable Integer id, HttpSession session, Model model) {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        UserDto userDto = userMapper.selectUserDtoByUserId(id);
        List<Star> stars = starMapper.selectStarsByUserId(loginUser.getId());
        List<FavoriteDto> favorites = new ArrayList<>();
        // 查询用户的评论数量
        userDto.setCommentNumber((dynamicCommentMapper.selectDynamicCommentNumberByUserId(userDto.getId()))
                + articleCommentMapper.selectCommentNumberByUserId(userDto.getId()));
        // 查询用户的话题数量
        userDto.setDynamicNumber(dynamicMapper.selectDynamicNumberByUserId(userDto.getId()));
        // 查询用户的粉丝
        List<UserDto> fans = followMapper.selectFans(id);
        // 查询用户的关注数量
        userDto.setFollowNumber(followMapper.selectFollowNumber(id));
        // 查询用户的粉丝数量
        userDto.setFanNumber(followMapper.selectFanNumber(id));
        // 查询用户的关注列表
        List<UserDto> follows = followMapper.selectFollowedUsers(id);
        // 查询用户的动态
        List<DynamicPreDto> dynamics = dynamicMapper.selectByUserId(id);
        // 查询用户的文章
        List<ArticlePreDto> articles = articleMapper.selectByUserId(id);
        if (loginUser != null) {
            // 查询该用户是否为自己关注
            userDto.setFollow(kkBlogService.ifFollow(loginUser.getId(), id));
            // 查询被访问用户是否是自己
            userDto.setMe(Objects.equals(loginUser.getId(), userDto.getId()));
            // 查询自己的关注列表
            List<Integer> followingIds = followMapper.selectFollowedIds(loginUser.getId());
            // 查询用户在自己的关注列表
            fans.forEach(item -> item.setFollow(followingIds.contains(item.getId())));
            follows.forEach(item -> item.setFollow(followingIds.contains(item.getId())));
            // 查询是否对用户的动态点赞
            dynamics.forEach(item -> {
                item.setLiked(likeMapper.selectIfUserLike(loginUser.getId() + "_" + item.getId()) > 0);

            });
            // 查询是否对用户的文章点赞
            articles.forEach(item -> {
                item.setLiked(kkBlogService.userLikedArticle(loginUser.getId(), item.getId()));

            });
        }
        getStar(stars, favorites);
        model.addAttribute("fans", fans);
        model.addAttribute("follows", follows);
        model.addAttribute("userDto", userDto);
        model.addAttribute("favorites", favorites);
        return "user-favorites";
    }

    private void getStar(List<Star> stars, List<FavoriteDto> favorites) {
        for (Star star : stars) {
            if (star.getType() == 1) { //文章
                Article article = articleMapper.selectById(star.getArticleId());
                FavoriteDto favorite = FavoriteDto.builder()
                        .id(star.getId())
                        .nickname(userMapper.selectById(article.getUserId()).getNickname())
                        .articleId(article.getId())
                        .stared(starMapper.selectIfUserStarArticle(star.getUserAndArticleId()) > 0)
                        .title(article.getTitle())
                        .preView(article.getPreview())
                        .content(article.getContent())
                        .likeNumber(article.getLikeNumber())
                        .starNumber(article.getStarNumber())
                        .viewNumber(article.getViewNumber())
                        .createTime(article.getCreateTime())
                        .tags(article.getTags())
                        .type(1)
                        .build();
                favorites.add(favorite);
            } else if (star.getType() == 0) { //动态(包括动态和话题)
                Dynamic dynamic = dynamicMapper.selectById(star.getDynamicId());
                if (dynamic.getType() == 0) { //动态
                    FavoriteDto favorite = FavoriteDto.builder()
                            .id(star.getId())
                            .nickname(userMapper.selectById(dynamic.getUserId()).getNickname())
                            .stared(starMapper.selectIfUserStar(star.getUserAndDynamicId()) > 0)
                            .dynamicId(dynamic.getId())
                            .title(dynamic.getTitle())
                            .preView(dynamic.getPreView())
                            .content(dynamic.getDynamic())
                            .likeNumber(dynamic.getLikeNumber())
                            .starNumber(dynamic.getStarNumber())
                            .viewNumber(dynamic.getViewNumber())
                            .createTime(dynamic.getCreateTime())
                            .tags(dynamic.getTags())
                            .type(0)
                            .build();
                    favorites.add(favorite);
                } else if (dynamic.getType() == 1) { //话题
                    FavoriteDto favorite = FavoriteDto.builder()
                            .id(star.getId())
                            .nickname(userMapper.selectById(dynamic.getUserId()).getNickname())
                            .dynamicId(dynamic.getId())
                            .stared(starMapper.selectIfUserStar(star.getUserAndDynamicId()) > 0)
                            .title(dynamic.getTitle())
                            .preView(dynamic.getPreView())
                            .content(dynamic.getTopicContent())
                            .likeNumber(dynamic.getLikeNumber())
                            .starNumber(dynamic.getStarNumber())
                            .viewNumber(dynamic.getViewNumber())
                            .createTime(dynamic.getCreateTime())
                            .tags(dynamic.getTags())
                            .type(0)
                            .build();
                    favorites.add(favorite);
                }

            }
        }
    }

    @ResponseBody
    @GetMapping("/favoritesMore")
    public ResponseDto favoritesLoadMore(@RequestParam("favoriteId") Integer favoriteId,
                                         @RequestParam("userId") Integer userId,
                                         HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        List<Star> stars = kkBlogService.selectMoreFavoritesByUserId(userId, favoriteId);
        List<FavoriteDto> favorites = new ArrayList<>();
        if (user != null) {
            getStar(stars, favorites);
        }
        return ResponseDto.Success("查询成功！", favorites);
    }
}
