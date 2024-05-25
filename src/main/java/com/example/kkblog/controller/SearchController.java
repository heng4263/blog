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
import java.util.stream.Collectors;

/**
 * @Author xiaoke
 * @Date 2024 04 11 00 11
 **/
@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private KKBlogService kkBlogService;



    @GetMapping("/results")
    public String searchResultsByKeywordAndNodeId(@RequestParam("keyword") String keyword, @RequestParam(value = "nodeId", required = false) Integer nodeId, HttpSession session, @NotNull Model model) {
        List<Search> searchs;
        if (nodeId == null) {
            searchs = kkBlogService.search(keyword);
        } else {
            String tag = getString(nodeId);
            searchs = kkBlogService.searchByKeywordAndNodeId(keyword, tag);
        }

        List<SearchDto> searchDtoList = searchs.stream().map(search -> {
            return SearchDto.builder()
                    .nickname(search.getNickname())
                    .articleId(search.getSource().equals("article") ? search.getId() : null)
                    .dynamicId(search.getSource().equals("dynamic") ? search.getId() : null)
                    .title(search.getTitle())
                    .preView(search.getPreView())
                    .createTime(search.getCreateTime())
                    .tags(search.getTags())
                    .userId(search.getUserId())
                    .source(search.getSource())
                    .build();
        }).collect(Collectors.toList());

        model.addAttribute("ranks", userMapper.selectScoresRank());
        model.addAttribute("searchs", searchDtoList);
        return "search";
    }

    @NotNull
    private static String getString(Integer nodeId) {
        String tag = "";
        switch (nodeId) {
            case 1:
                tag = "[\"全部\"]";
                break;
            case 2:
                tag = "[\"交流\"]";
                break;
            case 3:
                tag = "[\"提问\"]";
                break;
            case 4:
                tag = "[\"反馈\"]";
                break;
            default:
                tag = "[]";
                break;
        }
        return tag;
    }


}



