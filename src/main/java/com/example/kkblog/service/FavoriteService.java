//package com.example.kkblog.service;
//
//import com.example.kkblog.domain.Favorite;
//import com.example.kkblog.domain.Star;
//import com.example.kkblog.mapper.ArticleMapper;
//import com.example.kkblog.mapper.DynamicMapper;
//import com.example.kkblog.mapper.StarMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class FavoriteService {
//
//    @Autowired
//    private StarMapper starMapper;
//    @Autowired
//    private ArticleMapper articleMapper;
//    @Autowired
//    private DynamicMapper dynamicMapper;
//
//    public List<Favorite> getUserFavorites(Integer userId) {
//        List<Star> stars = starMapper.selectStarsByUserId(userId);
//        List<Favorite> favorites = new ArrayList<>();
//
//        for (Star star : stars) {
//            if ("article".equals(star.getType())) { // 假设Star有type属性来区分文章和动态
//                Article article = articleMapper.selectById(star.getArticleId());
//                Favorite favorite = Favorite.builder()
//                        .nickname(article.getUser().getNickname()) // 假设Article有User对象关联
//                        .title(article.getTitle())
//                        .content(article.getContent())
//                        .likeCount(article.getLikeCount())
//                        .commentCount(article.getCommentCount())
//                        .viewCount(article.getViewCount())
//                        .publishTime(article.getPublishTime())
//                        .tags(article.getTags())
//                        .type("article")
//                        .build();
//                favorites.add(favorite);
//            } else if ("dynamic".equals(star.getType())) {
//                Dynamic dynamic = dynamicMapper.selectById(star.getDynamicId());
//                Favorite favorite = Favorite.builder()
//                        .nickname(dynamic.getUser().getNickname()) // 假设Dynamic有User对象关联
//                        .title(dynamic.getTitle())
//                        .content(dynamic.getContent())
//                        .likeCount(dynamic.getLikeCount())
//                        .commentCount(dynamic.getCommentCount())
//                        .viewCount(dynamic.getViewCount())
//                        .publishTime(dynamic.getPublishTime())
//                        .tags(dynamic.getTags())
//                        .type("dynamic")
//                        .build();
//                favorites.add(favorite);
//            }
//        }
//
//        return favorites;
//    }
//}
