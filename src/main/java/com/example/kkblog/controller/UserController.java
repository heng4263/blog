package com.example.kkblog.controller;

import com.example.kkblog.controller.dto.ResponseDto;
import com.example.kkblog.domain.*;
import com.example.kkblog.domain.dto.ArticlePreDto;
import com.example.kkblog.domain.dto.DynamicPreDto;
import com.example.kkblog.domain.dto.UserDto;
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
 * @Author Hyh
 * @Date 2024 04 16 19 59
 **/
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DynamicMapper dynamicMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    @Autowired
    private DynamicCommentMapper dynamicCommentMapper;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private KKBlogService kkBlogService;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private StarMapper starMapper;

    @Autowired
    private ArticleCommentMapper articleCommentMapper;

    @GetMapping("/me")
    public String me(HttpSession session, Model model) {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        return userPage(loginUser.getId(), session, model);
    }

    @GetMapping("/{id}")
    public String userPage(@PathVariable Integer id, HttpSession session, Model model) {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        UserDto userDto = userMapper.selectUserDtoByUserId(id);
        userDto.setDynamicNumber(dynamicMapper.selectDynamicNumberByUserId(userDto.getId()));
        userDto.setCommentNumber((dynamicCommentMapper.selectDynamicCommentNumberByUserId(userDto.getId()))
                + articleCommentMapper.selectCommentNumberByUserId(userDto.getId()));
        List<UserDto> fans = followMapper.selectFans(id);
        userDto.setFollowNumber(followMapper.selectFollowNumber(id));
        userDto.setFanNumber(followMapper.selectFanNumber(id));
        List<UserDto> follows = followMapper.selectFollowedUsers(id);
        List<DynamicPreDto> dynamics = dynamicMapper.selectByUserId(id);
        List<ArticlePreDto> articles = articleMapper.selectByUserId(id);
        if (loginUser != null) {
            userDto.setFollow(kkBlogService.ifFollow(loginUser.getId(), id));
            userDto.setMe(Objects.equals(loginUser.getId(), userDto.getId()));
            List<Integer> followingIds = followMapper.selectFollowedIds(loginUser.getId());
            fans.forEach(item -> item.setFollow(followingIds.contains(item.getId())));
            follows.forEach(item -> item.setFollow(followingIds.contains(item.getId())));
            dynamics.forEach(item -> {
                item.setLiked(likeMapper.selectIfUserLike(loginUser.getId() + "_" + item.getId()) > 0);

            });
            articles.forEach(item -> {
                item.setLiked(kkBlogService.userLikedArticle(loginUser.getId(), item.getId()));

            });
        }
        model.addAttribute("fans", fans);
        model.addAttribute("follows", follows);
        model.addAttribute("userDto", userDto);
        model.addAttribute("dynamics", dynamics);
        model.addAttribute("articles", articles);
        return "user";
    }

    @ResponseBody
    @GetMapping("/more")
    public ResponseDto loadMore(@RequestParam("dynamicId") Integer dynamicId,
                                @RequestParam("userId") Integer userId,
                                HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        List<DynamicPreDto> dynamicPreDtos = kkBlogService.selectMoreDynamicsByUserId(userId, dynamicId);
        if (user != null) {
            dynamicPreDtos.forEach(item -> item.setLiked(likeMapper.selectIfUserLike(user.getId() + "_" + item.getId()) > 0));
        }
        return ResponseDto.Success("查询成功！", dynamicPreDtos);
    }


    @ResponseBody
    @GetMapping("/articleMore")
    public ResponseDto articleIdLoadMore(@RequestParam("articleId") Integer articleId,
                                         @RequestParam("userId") Integer userId,
                                         HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        List<ArticlePreDto> ArticlePreDtos = kkBlogService.selectMoreArticlesByUserId(userId, articleId);
        if (user != null) {
            // 查询是否对用户的文章点赞
            ArticlePreDtos.forEach(item -> {
                item.setLiked(kkBlogService.userLikedArticle(user.getId(), item.getId()));

            });
        }
        return ResponseDto.Success("查询成功！", ArticlePreDtos);
    }

    @GetMapping("/email")
    public String email(HttpSession session, Model model) {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        UserDto userDto = userMapper.selectUserDtoByUserId(loginUser.getId());
        List<User> users = userMapper.selectAllUser(loginUser.getUsername());
        userDto.setDynamicNumber(dynamicMapper.selectDynamicNumberByUserId(userDto.getId()));
        userDto.setCommentNumber((dynamicCommentMapper.selectDynamicCommentNumberByUserId(userDto.getId()))
                + articleCommentMapper.selectCommentNumberByUserId(userDto.getId()));
        List<UserDto> fans = followMapper.selectFans(loginUser.getId());
        userDto.setFollowNumber(followMapper.selectFollowNumber(loginUser.getId()));
        userDto.setFanNumber(followMapper.selectFanNumber(loginUser.getId()));
        List<UserDto> follows = followMapper.selectFollowedUsers(loginUser.getId());
        if (loginUser != null) {
            userDto.setFollow(kkBlogService.ifFollow(loginUser.getId(), loginUser.getId()));
            userDto.setMe(Objects.equals(loginUser.getId(), userDto.getId()));
            List<Integer> followingIds = followMapper.selectFollowedIds(loginUser.getId());
            fans.forEach(item -> item.setFollow(followingIds.contains(item.getId())));
            follows.forEach(item -> item.setFollow(followingIds.contains(item.getId())));
        }
        model.addAttribute("fans", fans);
        model.addAttribute("follows", follows);
        model.addAttribute("userDto", userDto);
        model.addAttribute("users", users);
        return "user-email";
    }
}
