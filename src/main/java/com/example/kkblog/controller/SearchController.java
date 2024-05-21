package com.example.kkblog.controller;

import com.example.kkblog.controller.dto.ResponseDto;
import com.example.kkblog.domain.Article;
import com.example.kkblog.domain.Dynamic;
import com.example.kkblog.domain.Star;
import com.example.kkblog.domain.dto.*;
import com.example.kkblog.mapper.*;
import com.example.kkblog.service.KKBlogService;
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
@RequestMapping("/search")
public class SearchController {


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


    @GetMapping("/like")
    @ResponseBody
    public ResponseDto search(@RequestParam("searchTerm") String searchTerm, HttpSession session, Model model) {
        List<SearchDto> searchDtoList = kkBlogService.search(searchTerm);
        return ResponseDto.Success("查询成功！", searchDtoList);

    }

//    @ResponseBody
//    @GetMapping("/favoritesMore")
//    public ResponseDto favoritesLoadMore(@RequestParam("favoriteId") Integer favoriteId,
//                                         @RequestParam("userId") Integer userId,
//                                         HttpSession session) {
//        UserDto user = (UserDto) session.getAttribute("user");
//        List<Star> stars = kkBlogService.selectMoreFavoritesByUserId(userId, favoriteId);
//        List<FavoriteDto> favorites = new ArrayList<>();
//        if (user != null) {
//            getStar(stars, favorites);
//        }
//        return ResponseDto.Success("查询成功！", favorites);
//    }
}
