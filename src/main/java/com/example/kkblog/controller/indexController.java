package com.example.kkblog.controller;

import com.example.kkblog.controller.dto.ResponseDto;
import com.example.kkblog.domain.Dynamic;
import com.example.kkblog.domain.Topic;
import com.example.kkblog.domain.dto.ArticlePreDto;
import com.example.kkblog.domain.dto.DynamicPreDto;
import com.example.kkblog.domain.dto.TopicDto;
import com.example.kkblog.domain.dto.UserDto;
import com.example.kkblog.mapper.*;
import com.example.kkblog.service.KKBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Hyh
 * @date 2024/4/7
 */
@Controller
@RequestMapping({"/index"})
public class indexController {
    @Autowired
    private DynamicMapper dynamicMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private KKBlogService kkBlogService;

    @Autowired
    private ArticleMapper articleMapper;

    @RequestMapping({"/", ""})
    public String index(Model model, HttpSession session) {
        // 查询用户排行榜
        model.addAttribute("ranks", userMapper.selectScoresRank());
        // 查询主题
        List<TopicDto> topicDtos = topicMapper.selectAvailableTopics();
        if (!topicDtos.isEmpty()) {
            topicDtos.get(0).setSelected(true);
        }
        model.addAttribute("topics", topicDtos);
        //查询用户点赞、评价的文章、动态等
        List<DynamicPreDto> dynamicPreDtos = dynamicMapper.selectNewestDynamics();
        UserDto user = (UserDto) session.getAttribute("user");
        if (user != null) {
            dynamicPreDtos.forEach(item -> {
                item.setLiked(likeMapper.selectIfUserLike(user.getId() + "_" + item.getId()) > 0);
            });
        }
        model.addAttribute("dynamics", dynamicPreDtos);
        return "index";
    }

//    @SuppressWarnings("all")

    //查询被选中的主题
    @RequestMapping("/topics/{id}")
    public String topics(@PathVariable int id, Model model, HttpSession session) {
        // 查询用户排行榜
        model.addAttribute("ranks", userMapper.selectScoresRank());
        // 查询所有主题
        List<TopicDto> topicDtos = topicMapper.selectAvailableTopics();
        TopicDto selectedTopic = null;
        //查询被选中的主题
        for (TopicDto topicDto : topicDtos) {
            if (topicDto.getId() == id) {
                topicDto.setSelected(true);
                selectedTopic = topicDto;
                break;
            }
        }
        //判断是否登录
        UserDto loginUser = (UserDto) session.getAttribute("user");
        model.addAttribute("topics", topicDtos);
        List<DynamicPreDto> dynamicPreDtos;
        //断言(必须放回true，否则抛出AssertionError异常)
        assert selectedTopic != null;
        if ("最新".equals(selectedTopic.getName())) {
            dynamicPreDtos = dynamicMapper.selectNewestDynamics();
        } else if ("推荐".equals(selectedTopic.getName())) {
            dynamicPreDtos = dynamicMapper.selectRecommendDynamics();
        } else if ("关注".equals(selectedTopic.getName())) {
            if (loginUser != null) {
                dynamicPreDtos = dynamicMapper.selectFollowDynamics(loginUser.getId());
            } else {
                dynamicPreDtos = new ArrayList<>();
            }
        } else {
            dynamicPreDtos = dynamicMapper.selectDynamicsByTopicId(id);
        }
        if (loginUser != null) {
            dynamicPreDtos.forEach(item -> {
                item.setLiked(likeMapper.selectIfUserLike(loginUser.getId() + "_" + item.getId()) > 0);
            });
        }
        model.addAttribute("dynamics", dynamicPreDtos);
        return "index";
    }


    // 加载更多
    @ResponseBody
    @GetMapping("/more")
    public ResponseDto loadMore(@RequestParam("dynamicId") Integer dynamicId,
                                @RequestParam("topicId") Integer topicId,
                                @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                HttpSession session) {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        Topic topic = topicMapper.selectById(topicId);
        List<DynamicPreDto> dynamicPreDtos;
        if (topic.getName().equals("最新")) {
            dynamicPreDtos = dynamicMapper.selectMoreNewestDynamics(dynamicId);
        } else if (topic.getName().equals("推荐")) {
            dynamicPreDtos = dynamicMapper.selectMoreRecommendDynamics(pageNum * 8);
        } else if (topic.getName().equals("关注")) {
            if (loginUser == null) {
                dynamicPreDtos = new ArrayList<>();
            } else {
                dynamicPreDtos = kkBlogService.selectMoreFollowDynamics(loginUser.getId(), dynamicId);
            }
        } else {
            Dynamic query = new Dynamic();
            query.setId(dynamicId);
            query.setTopicId(topicId);
            dynamicPreDtos = dynamicMapper.selectMoreDynamicsByTopicId(query);
        }
        if (loginUser != null) {
            dynamicPreDtos.forEach(item -> {
                item.setLiked(likeMapper.selectIfUserLike(loginUser.getId() + "_" + item.getId()) > 0);
            });
        }
        return ResponseDto.Success("查询成功！", dynamicPreDtos);
    }

    //文章
    @GetMapping("/article")
    public String article(Model model) {
        // 查询用户排行榜
        model.addAttribute("ranks", userMapper.selectScoresRank());
        List<ArticlePreDto> articlePreDtos = articleMapper.selectNewestArticles();
        model.addAttribute("articles", articlePreDtos);
        return "article";
    }


}
