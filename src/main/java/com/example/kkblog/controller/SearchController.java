package com.example.kkblog.controller;

import com.example.kkblog.controller.dto.ResponseDto;
import com.example.kkblog.domain.Article;
import com.example.kkblog.domain.Dynamic;
import com.example.kkblog.domain.Search;
import com.example.kkblog.domain.Star;
import com.example.kkblog.domain.dto.*;
import com.example.kkblog.mapper.*;
import com.example.kkblog.service.KKBlogService;
import org.jetbrains.annotations.NotNull;
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


    @GetMapping("/results")
    public String searchResults(@RequestParam("keyword") String keyword, Model model) {
        List<Search> searchs = kkBlogService.search(keyword);
        List<SearchDto> searchDtoList = new ArrayList<>();

        for (Search search : searchs) {
            if (search.getSource().equals("article")) {//文章
                SearchDto searchDto = SearchDto.builder()
                        .articleId(search.getId())
                        .title(search.getTitle())
                        .preView(search.getPreView())
                        .createTime(search.getCreateTime())
                        .userId(search.getUserId())
                        .nickname(search.getNickname())
                        .source(search.getSource())
                        .build();
                searchDtoList.add(searchDto);
            } else if (search.getSource().equals("dynamic")) { //动态
                SearchDto searchDto = SearchDto.builder()
                        .dynamicId(search.getId())
                        .title(search.getTitle())
                        .preView(search.getPreView())
                        .createTime(search.getCreateTime())
                        .userId(search.getUserId())
                        .nickname(search.getNickname())
                        .source(search.getSource())
                        .build();
                searchDtoList.add(searchDto);
            }
        }
        model.addAttribute("searchs", searchDtoList);
        return "search"; // 返回显示结果的视图
    }

    @GetMapping("/like")
    @ResponseBody
    public ResponseDto search(@RequestParam("keyword") String keyword, HttpSession session, @NotNull Model model) {
        List<Search> searchList = kkBlogService.search(keyword);
        return ResponseDto.Success("查询成功！", searchList);
    }


}



