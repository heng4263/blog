package com.example.kkblog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.kkblog.config.ConstParam;
import com.example.kkblog.config.exception.KKBlogException;
import com.example.kkblog.controller.dto.ResponseDto;
import com.example.kkblog.domain.*;
import com.example.kkblog.domain.dto.DynamicCommentDto;
import com.example.kkblog.domain.dto.DynamicDetailDto;
import com.example.kkblog.domain.dto.TopicDto;
import com.example.kkblog.domain.dto.UserDto;
import com.example.kkblog.mapper.*;
import com.example.kkblog.service.KKBlogService;
import com.example.kkblog.util.IPUtil;
import com.example.kkblog.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author xiaoke
 * @Date 2024 04 12 19 42
 **/
@Controller
@RequestMapping("/dynamic")
public class dynamicController {

    @Autowired
    private KKBlogService kkBlogService;

    @Autowired
    private DynamicMapper dynamicMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DynamicCommentMapper dynamicCommentMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private StarMapper starMapper;

    @Autowired
    private CommentLikeMapper commentLikeMapper;

    @GetMapping("/create")
    public String create(Model model) {
        List<TopicDto> topicDtos = topicMapper.selectAvailableTopics();
        String filterTopics = "最新,推荐,关注";
        model.addAttribute("topics", topicDtos.stream().filter(i -> !filterTopics.contains(i.getName())).collect(Collectors.toList()));
        return "create";
    }

    @GetMapping("/create-topic")
    public String createTopic(Model model) {
        List<TopicDto> topicDtos = topicMapper.selectAvailableTopics();
        String filterTopics = "最新,推荐,关注";
        model.addAttribute("topics", topicDtos.stream().filter(i -> !filterTopics.contains(i.getName())).collect(Collectors.toList()));
        return "topic-create";
    }

    // 发布动态
    @ResponseBody
    @PostMapping("/post")
    public ResponseDto postDynamic(Dynamic dynamic, HttpSession session) throws KKBlogException {
        StringUtils.trimObject(dynamic);
        if (dynamic.getTopicId() == null) {
            return ResponseDto.Fail("请选择话题！");
        }
        if (dynamic.getType() == 0) {
            if (StringUtils.isEmpty(dynamic.getDynamic())) {
                return ResponseDto.Fail("请输入您想分享的动态内容！");
            }
            // 设置预览文字
            if (dynamic.getDynamic().length() > 200) {
                dynamic.setPreView(dynamic.getDynamic().substring(0, 200) + "...");
            } else {
                dynamic.setPreView(dynamic.getDynamic());
            }
        } else if (dynamic.getType() == 1) {
            if (StringUtils.isEmpty(dynamic.getTitle())) {
                return ResponseDto.Fail("请输入话题的标题！");
            }
            // 设置预览文字
            dynamic.setPreView(dynamic.getPreView().replaceAll("<", "《").replaceAll(">", "》"));
            if (dynamic.getPreView().length() > 200) {
                dynamic.setPreView(dynamic.getPreView().substring(0, 200) + "...");
            }
        }
        // 设置用户id
        UserDto user = (UserDto) session.getAttribute("user");
        dynamic.setUserId(user.getId());
        // 获取ip地址
        dynamic.setIpLocation(user.getIpLocation());
        // 获取标签
        String tagString = dynamic.getTags();
        if (!StringUtils.isEmpty(tagString)) {
            JSONArray tags = JSON.parseArray(tagString);
            for (Object tag : tags) {
                kkBlogService.quoteATag((String) tag);
            }
        }
        // 设置状态
        dynamic.setStatus(0);
        // 插入数据库
        dynamicMapper.insert(dynamic);
        // 加积分
        kkBlogService.addUserScores(user.getId(), "发布动态", ConstParam.DYNAMIC_SCORES);
        return ResponseDto.Success("发布成功！", dynamic);
    }

    // 获取动态文章点赞信息
    @ResponseBody
    @GetMapping("/likes")
    public ResponseDto getLikes(@RequestParam("dynamicId") Integer dynamicId) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dynamic_id", dynamicId);
        return ResponseDto.Success(String.valueOf(likeMapper.selectCount(queryWrapper)), likeMapper.selectLikeUserByDynamicId(dynamicId));
    }

    // 动态详情
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") String id, Model model, HttpSession session) {
        DynamicDetailDto dynamicDetailDto = dynamicMapper.selectDetailById(id);
        if (dynamicDetailDto == null) {
            return "error/404";
        }
        UserDto userDto = userMapper.selectUserDtoByUserId(dynamicDetailDto.getUserId());
        // 查询用户话题数量
        userDto.setDynamicNumber(dynamicMapper.selectDynamicNumberByUserId(userDto.getId()));
        // 查询用户评论数量
        userDto.setCommentNumber(dynamicCommentMapper.selectDynamicCommentNumberByUserId(userDto.getId()));
        model.addAttribute("userInfo", userDto);
        dynamicMapper.addViewById(id);
        // 查询动态文章的点赞用户
        model.addAttribute("likes", likeMapper.selectLikeUserByDynamicId(dynamicDetailDto.getId()));
        // 查询所有评论(父评论)
        List<DynamicCommentDto> dynamicCommentDtos = dynamicCommentMapper.selectParentCommentByDynamicId(dynamicDetailDto.getId());
        // 查询动态文章的收藏用户
        model.addAttribute("star", starMapper.selectStarUserByDynamicId(dynamicDetailDto.getId()));
        List<Integer> likedCommentIds = new ArrayList<>();
        // 获取当前登录用户
        UserDto loginUser = (UserDto) session.getAttribute("user");
        if (loginUser != null) {
            CommentLike commentLike = new CommentLike();
            commentLike.setUserId(loginUser.getId());
            commentLike.setDynamicId(dynamicDetailDto.getId());
            // 查询用户所有评论是否被点赞
            likedCommentIds = commentLikeMapper.selectLikedCommentIds(commentLike);
            // 查询动态是否被点赞、收藏
            dynamicDetailDto.setLiked(likeMapper.selectIfUserLike(loginUser.getId() + "_" + dynamicDetailDto.getId()) > 0);
            dynamicDetailDto.setStared(starMapper.selectIfUserStar(loginUser.getId() + "_" + dynamicDetailDto.getId()) > 0);
        }
        // 遍历所有父评论
        for (DynamicCommentDto item : dynamicCommentDtos) {
            // 设置被点赞的父评论
            item.setLiked(likedCommentIds.contains(item.getId()));
            // 查询该父评论下的所有子评论
            List<DynamicCommentDto> childComments = dynamicCommentMapper.selectChildCommentByParentId(item.getId());
            for (DynamicCommentDto childComment : childComments) {
                // 设置被点赞的子评论
                childComment.setLiked(likedCommentIds.contains(childComment.getId()));
            }
            item.setChildren(childComments);
        }
        model.addAttribute("dynamic", dynamicDetailDto);
        model.addAttribute("comments", dynamicCommentDtos);
        return "dynamic";
    }

    // 发表评论
    @ResponseBody
    @PostMapping("/comment/post")
    public ResponseDto postComment(DynamicComment dynamicComment, HttpSession session) throws KKBlogException {
        //去空格
        StringUtils.trimObject(dynamicComment);
        if (StringUtils.isEmpty(dynamicComment.getContent())) {
            return ResponseDto.Fail("请输入评论内容");
        }
        if (dynamicComment.getParentId() == null) {
            //如果该评论没有父评论，则该评论为父评论
            dynamicComment.setParentId(-1);
        } else if (-1 != dynamicComment.getParentId()) {
            DynamicComment parentComment = dynamicCommentMapper.selectById(dynamicComment.getParentId());
            if (-1 != parentComment.getParentId()) {
                // 如果该评论的父评论的父评论不为-1，则设置该评论的父评论的父评论为父评论
                dynamicComment.setParentId(parentComment.getParentId());
            }
            dynamicComment.setReplyUserId(parentComment.getUserId());
            dynamicComment.setReplyContent(parentComment.getContent());
        }
        UserDto user = (UserDto) session.getAttribute("user");
        dynamicComment.setIpLocation(user.getIpLocation());
        dynamicComment.setUserId(user.getId());
        dynamicCommentMapper.insert(dynamicComment);
        if (-1 == dynamicComment.getParentId()) {
            return ResponseDto.Success("评论成功！", dynamicCommentMapper.selectParentByCommentId(dynamicComment.getId()));
        }
        return ResponseDto.Success("评论成功！", dynamicCommentMapper.selectChildByCommentId(dynamicComment.getId()));
    }

    // 点赞动态
    @ResponseBody
    @GetMapping("/like")
    public ResponseDto doLikeDynamic(@RequestParam("dynamicId") Integer dynamicId, HttpSession session) {
        Like like = new Like();
        like.setDynamicId(dynamicId);
        UserDto user = (UserDto) session.getAttribute("user");
        like.setUserId(user.getId());
        like.setUserAndDynamicId(user.getId() + "_" + dynamicId);
        if (likeMapper.selectIfUserLike(like.getUserAndDynamicId()) > 0) {
            return ResponseDto.Fail("您已经点过赞咯~");
        }
        likeMapper.insert(like);
        dynamicMapper.addLikeById(dynamicId);
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dynamic_id", dynamicId);
        // 返回点赞总数和具体的点赞用户列表
        return ResponseDto.Success(String.valueOf(likeMapper.selectCount(queryWrapper)), likeMapper.selectLikeUserByDynamicId(dynamicId));
    }

    // 取消点赞动态
    @ResponseBody
    @GetMapping("/unlike")
    public ResponseDto doUnlikeDynamic(@RequestParam("dynamicId") Integer dynamicId, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        likeMapper.deleteByUserDynamicId(user.getId() + "_" + dynamicId);
        dynamicMapper.cancelLikeById(dynamicId);
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dynamic_id", dynamicId);
        return ResponseDto.Success(String.valueOf(likeMapper.selectCount(queryWrapper)), likeMapper.selectLikeUserByDynamicId(dynamicId));
    }

    //收藏动态
    @ResponseBody
    @GetMapping("/star")
    public ResponseDto doStarDynamic(@RequestParam("dynamicId") Integer dynamicId, HttpSession session) {
        Star star = new Star();
        star.setDynamicId(dynamicId);
        UserDto user = (UserDto) session.getAttribute("user");
        star.setUserId(user.getId());
        star.setUserAndDynamicId(user.getId() + "_" + dynamicId);
        star.setType(0);
        if (starMapper.selectIfUserStar(star.getUserAndDynamicId()) > 0) {
            return ResponseDto.Fail("您已经收藏咯~");
        }
        starMapper.insert(star);
        dynamicMapper.addStarById(dynamicId);
        QueryWrapper<Star> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dynamic_id", dynamicId);
        // 返回收藏总数和具体的收藏用户列表
        return ResponseDto.Success(String.valueOf(starMapper.selectCount(queryWrapper)), starMapper.selectStarUserByDynamicId(dynamicId));
    }

    // 取消收藏动态
    @ResponseBody
    @GetMapping("/unstar")
    public ResponseDto doUnStarDynamic(@RequestParam("dynamicId") Integer dynamicId, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        starMapper.deleteByUserDynamicId(user.getId() + "_" + dynamicId);
        dynamicMapper.cancelStarById(dynamicId);
        QueryWrapper<Star> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dynamic_id", dynamicId);
        return ResponseDto.Success(String.valueOf(starMapper.selectCount(queryWrapper)), starMapper.selectStarUserByDynamicId(dynamicId));
    }

    // 加载更多评论
    @ResponseBody
    @GetMapping("/comment/more")
    public ResponseDto loadMore(@RequestParam("dynamicId") Integer dynamicId,
                                @RequestParam("commentId") Integer commentId,
                                HttpSession session) {
        DynamicComment dynamicComment = dynamicCommentMapper.selectById(commentId);
        // 查询登录用户的点赞
        List<Integer> likedCommentIds = new ArrayList<>();
        UserDto loginUser = (UserDto) session.getAttribute("user");
        if (loginUser != null) {
            CommentLike commentLike = new CommentLike();
            commentLike.setUserId(loginUser.getId());
            commentLike.setDynamicId(dynamicId);
            // 登录用户的所有点赞
            likedCommentIds = commentLikeMapper.selectLikedCommentIds(commentLike);
        }
        // 加载更多父亲评论
        if (-1 == dynamicComment.getParentId()) {
            DynamicComment query = new DynamicComment();
            query.setId(commentId);
            query.setDynamicId(dynamicId);
            // 查询被隐藏的父评价
            List<DynamicCommentDto> dynamicCommentDtos = dynamicCommentMapper.selectMoreParentCommentByDynamicId(query);
            // 设置加载父评论和该父评论的子评论点赞
            for (DynamicCommentDto item : dynamicCommentDtos) {
                item.setLiked(likedCommentIds.contains(item.getId()));
                List<DynamicCommentDto> childComments = dynamicCommentMapper.selectChildCommentByParentId(item.getId());
                for (DynamicCommentDto childComment : childComments) {
                    childComment.setLiked(likedCommentIds.contains(childComment.getId()));
                }
                item.setChildren(childComments);
            }
            return ResponseDto.Success("查询成功", dynamicCommentDtos);
        }
        // 加载更多子评论
        else {
            DynamicComment query = dynamicCommentMapper.selectById(commentId);
            List<DynamicCommentDto> dynamicCommentDtos = dynamicCommentMapper.selectMoreChildCommentByParent(query);
            for (DynamicCommentDto childComment : dynamicCommentDtos) {
                childComment.setLiked(likedCommentIds.contains(childComment.getId()));
            }
            return ResponseDto.Success("查询成功", dynamicCommentDtos);
        }
    }

    // 点赞评论
    @ResponseBody
    @GetMapping("/comment/like")
    public ResponseDto doLikeComment(@RequestParam("commentId") Integer commentId, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return ResponseDto.Fail("请登录后进行操作~");
        }
        // 检查是否已经点赞
        if (commentLikeMapper.selectIfUserLike(user.getId() + "_" + commentId) > 0) {
            return ResponseDto.Fail("你已经点过赞咯~");
        }
        // 查询被点赞的评论
        DynamicComment dynamicComment = dynamicCommentMapper.selectById(commentId);
        // 新建对象
        CommentLike commentLike = new CommentLike();
        commentLike.setCommentId(commentId);
        commentLike.setUserId(user.getId());
        commentLike.setUserAndCommentId(user.getId() + "_" + commentId);
        commentLike.setLikedUserId(dynamicComment.getUserId());
        commentLike.setDynamicId(dynamicComment.getDynamicId());
        // 插入数据库
        commentLikeMapper.insert(commentLike);
        // 更新评论点赞数量
        dynamicCommentMapper.addLikeById(commentId);
        return ResponseDto.Success("点赞成功", dynamicCommentMapper.selectById(commentId));
    }

    // 取消点赞评论
    @ResponseBody
    @GetMapping("/comment/unlike")
    public ResponseDto doUnlikeComment(@RequestParam("commentId") Integer commentId, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return ResponseDto.Fail("请登录后进行操作~");
        }
        // 检查是否已经点赞
        if (commentLikeMapper.selectIfUserLike(user.getId() + "_" + commentId) == 0) {
            return ResponseDto.Fail("你还没有对这条评论点过赞~");
        }
        // 删除点赞数据
        commentLikeMapper.deleteByUserCommentId(user.getId() + "_" + commentId);
        // 更新评论点赞数量
        dynamicCommentMapper.cancelLikeById(commentId);
        return ResponseDto.Success("取消点赞成功", dynamicCommentMapper.selectById(commentId));
    }

    //删除动态
    @ResponseBody
    @GetMapping("/delete")
    public ResponseDto deleteDynamic(@RequestParam("id") Integer id, HttpSession session) {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        if (loginUser == null) {
            return ResponseDto.Fail("请登录后进行操作");
        }
        DynamicDetailDto dynamicDetailDto = dynamicMapper.selectDetailById(String.valueOf(id));
        if (dynamicDetailDto == null || !Objects.equals(dynamicDetailDto.getUserId(), loginUser.getId())) {
            return ResponseDto.Fail("权限验证失败！");
        }
        if (dynamicMapper.deleteDynamicById(id) == 0) {
            return ResponseDto.Fail("操作失败：未找到删除对象");
        }
        return ResponseDto.Success("操作成功", null);
    }
}
