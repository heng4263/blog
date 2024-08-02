package com.example.kkblog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.kkblog.config.ConstParam;
import com.example.kkblog.config.exception.KKBlogException;
import com.example.kkblog.controller.dto.ResponseDto;
import com.example.kkblog.domain.*;
import com.example.kkblog.domain.dto.*;
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
 * @Author Hyh
 * @Date 2024 04 27 23 12
 **/
@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private KKBlogService kkBlogService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DynamicMapper dynamicMapper;

    @Autowired
    private DynamicCommentMapper dynamicCommentMapper;

    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    @Autowired
    private ArticleCommentMapper articleCommentMapper;

    @Autowired
    private StarMapper starMapper;

    @Autowired
    private ArticleCommentLikeMapper articleCommentLikeMapper;

    @GetMapping("/create")
    public String create() {
        return "article-create";
    }

    @GetMapping("/detail")
    public String detail() {
        return "article-detail";
    }

    // 文章详情
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") String id, Model model, HttpSession session) {
        ArticleDetailDto articleDetailDto = articleMapper.selectDetailById(id);
        if (articleDetailDto == null) {
            return "error/404";
        }
        UserDto userDto = userMapper.selectUserDtoByUserId(articleDetailDto.getUserId());
        // 查询用户话题数量
        userDto.setDynamicNumber(dynamicMapper.selectDynamicNumberByUserId(userDto.getId()));
        userDto.setCommentNumber(dynamicCommentMapper.selectDynamicCommentNumberByUserId(userDto.getId()));
        model.addAttribute("userInfo", userDto);
        articleMapper.addViewById(id);
        model.addAttribute("likes", articleLikeMapper.selectLikeUserByArticleId(articleDetailDto.getId()));
        model.addAttribute("star", starMapper.selectStarUserByArticleId(articleDetailDto.getId()));
        List<ArticleCommentDto> articleCommentDtos = articleCommentMapper.selectParentCommentByArticleId(articleDetailDto.getId());
        List<Integer> likedCommentIds = new ArrayList<>();
        UserDto loginUser = (UserDto) session.getAttribute("user");
        if (loginUser != null) {
            articleDetailDto.setLiked(kkBlogService.userLikedArticle(loginUser.getId(), articleDetailDto.getId()));
            articleDetailDto.setStared(starMapper.selectIfUserStarArticle(loginUser.getId() + "_" + articleDetailDto.getId()) > 0);
            ArticleCommentLike articleCommentLike = new ArticleCommentLike();
            articleCommentLike.setUserId(loginUser.getId());
            articleCommentLike.setArticleId(articleDetailDto.getId());
            likedCommentIds = articleCommentLikeMapper.selectLikedCommentIds(articleCommentLike);

        }
        for (ArticleCommentDto item : articleCommentDtos) {
            item.setLiked(likedCommentIds.contains(item.getId()));
            List<ArticleCommentDto> childComments = articleCommentMapper.selectChildCommentByParentId(item.getId());
            for (ArticleCommentDto childComment : childComments) {
                childComment.setLiked(likedCommentIds.contains(childComment.getId()));
            }
            item.setChildren(childComments);
        }
        model.addAttribute("article", articleDetailDto);
        model.addAttribute("comments", articleCommentDtos);
        return "article-detail";
    }

    // 发布文章
    @ResponseBody
    @PostMapping("/post")
    public ResponseDto postArticle(Article article, HttpSession session) throws KKBlogException {
        StringUtils.trimObject(article);
        if (StringUtils.isEmpty(article.getTitle())) {
            return ResponseDto.Fail("请输入文章的标题！");
        }
        // 设置预览文字
        article.setPreview(article.getPreview().replaceAll("<", "《").replaceAll(">", "》"));
        // 设置用户id
        UserDto loginUser = (UserDto) session.getAttribute("user");
        article.setUserId(loginUser.getId());
        // 获取ip地址
        article.setIpLocation(loginUser.getIpLocation());
        String tagString = article.getTags();
        if (!StringUtils.isEmpty(tagString)) {
            JSONArray tags = JSON.parseArray(tagString);
            for (Object tag : tags) {
                kkBlogService.quoteATag((String) tag);
            }
        }
        article.setStatus(0);
        articleMapper.insert(article);
        kkBlogService.addUserScores(loginUser.getId(), "发布动态", ConstParam.ARTICLE_SCORES);
        return ResponseDto.Success("发布成功！", article);
    }

    // 发表评论
    @ResponseBody
    @PostMapping("/comment/post")
    public ResponseDto postComment(ArticleComment articleComment, HttpSession session) throws KKBlogException {
        StringUtils.trimObject(articleComment);
        if (StringUtils.isEmpty(articleComment.getContent())) {
            return ResponseDto.Fail("请输入评论内容");
        }
        if (articleComment.getParentId() == null) {
            articleComment.setParentId(-1);
        } else if(-1 != articleComment.getParentId()){
            ArticleComment parentComment = articleCommentMapper.selectById(articleComment.getParentId());
            if (-1 != parentComment.getParentId()) {
                articleComment.setParentId(parentComment.getParentId());
            }
            articleComment.setReplyUserId(parentComment.getUserId());
            articleComment.setReplyContent(parentComment.getContent());
        }
        UserDto loginUser = (UserDto) session.getAttribute("user");
        articleComment.setIpLocation(loginUser.getIpLocation());
        articleComment.setUserId(loginUser.getId());
        articleCommentMapper.insert(articleComment);
        if (-1 == articleComment.getParentId()) {
            return ResponseDto.Success("评论成功！", articleCommentMapper.selectParentByCommentId(articleComment.getId()));
        }
        return ResponseDto.Success("评论成功！", articleCommentMapper.selectChildByCommentId(articleComment.getId()));
    }

    // 点赞文章
    @ResponseBody
    @GetMapping("/like")
    public ResponseDto doLikeArticle(@RequestParam("articleId") Integer articleId, HttpSession session) {
        ArticleLike articleLike = new ArticleLike();
        articleLike.setArticleId(articleId);
        UserDto loginUser = (UserDto) session.getAttribute("user");
        articleLike.setUserId(loginUser.getId());
        if (kkBlogService.userLikedArticle(loginUser.getId(), articleId)) {
            return ResponseDto.Fail("您已经点过赞咯~");
        }
        articleLikeMapper.insert(articleLike);
        articleMapper.addLikeById(articleId);
        QueryWrapper<ArticleLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId);
        return ResponseDto.Success(String.valueOf(articleLikeMapper.selectCount(queryWrapper)), articleLikeMapper.selectLikeUserByArticleId(articleId));
    }

    // 取消点赞动态
    @ResponseBody
    @GetMapping("/unlike")
    public ResponseDto doUnlikeArticle(@RequestParam("articleId") Integer articleId, HttpSession session) {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        ArticleLike articleLike = new ArticleLike();
        articleLike.setArticleId(articleId);
        articleLike.setUserId(loginUser.getId());
        articleLikeMapper.deleteByUserLike(articleLike);
        articleMapper.cancelLikeById(articleId);
        QueryWrapper<ArticleLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId);
        return ResponseDto.Success(String.valueOf(articleLikeMapper.selectCount(queryWrapper)), articleLikeMapper.selectLikeUserByArticleId(articleId));
    }

    //收藏文章
    @ResponseBody
    @GetMapping("/star")
    public ResponseDto doStarArticle(@RequestParam("articleId") Integer articleId, HttpSession session) {
        Star star = new Star();
        star.setArticleId(articleId);
        UserDto user = (UserDto) session.getAttribute("user");
        star.setUserId(user.getId());
        star.setType(1);
        star.setUserAndArticleId(user.getId() + "_" + articleId);
        if (starMapper.selectIfUserStarArticle(star.getUserAndArticleId()) > 0) {
            return ResponseDto.Fail("您已经收藏咯~");
        }
        starMapper.insert(star);
        articleMapper.addStarById(articleId);
        QueryWrapper<Star> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId);
        // 返回收藏总数和具体的收藏用户列表
        return ResponseDto.Success(String.valueOf(starMapper.selectCount(queryWrapper)), starMapper.selectStarUserByArticleId(articleId));

    }

    // 取消收藏动态
    @ResponseBody
    @GetMapping("/unstar")
    public ResponseDto doUnStarArticle(@RequestParam("articleId") Integer articleId, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        starMapper.deleteByUserArticleId(user.getId() + "_" + articleId);
        articleMapper.cancelStarById(articleId);
        QueryWrapper<Star> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dynamic_id", articleId);
        return ResponseDto.Success(String.valueOf(starMapper.selectCount(queryWrapper)), starMapper.selectStarUserByArticleId(articleId));
    }

    // 加载更多评论
    @ResponseBody
    @GetMapping("/comment/more")
    public ResponseDto loadMore(@RequestParam("articleId") Integer articleId,
                                @RequestParam("commentId") Integer commentId,
                                HttpSession session) {
        ArticleComment articleComment = articleCommentMapper.selectById(commentId);
        // 查询用户的点赞;
        List<Integer> likedCommentIds = new ArrayList<>();
        UserDto loginUser = (UserDto) session.getAttribute("user");
        if (loginUser != null) {
            ArticleCommentLike articleCommentLike = new ArticleCommentLike();
            articleCommentLike.setUserId(loginUser.getId());
            articleCommentLike.setArticleId(articleId);
            likedCommentIds = articleCommentLikeMapper.selectLikedCommentIds(articleCommentLike);
        }
        // 加载更多父亲评论
        if (-1 == articleComment.getParentId()) {
            ArticleComment query = new ArticleComment();
            query.setId(commentId);
            query.setArticleId(articleId);
            List<ArticleCommentDto> articleCommentDtos = articleCommentMapper.selectMoreParentCommentByArticleId(query);
            for (ArticleCommentDto item : articleCommentDtos) {
                item.setLiked(likedCommentIds.contains(item.getId()));
                List<ArticleCommentDto> childComments = articleCommentMapper.selectChildCommentByParentId(item.getId());
                for (ArticleCommentDto childComment : childComments) {
                    childComment.setLiked(likedCommentIds.contains(childComment.getId()));
                }
                item.setChildren(childComments);
            }
            return ResponseDto.Success("查询成功", articleCommentDtos);
        }
        // 加载更多子评论
        else {
            ArticleComment query = articleCommentMapper.selectById(commentId);
            List<ArticleCommentDto> articleCommentDtos = articleCommentMapper.selectMoreChildCommentByParent(query);
            for (ArticleCommentDto childComment : articleCommentDtos) {
                childComment.setLiked(likedCommentIds.contains(childComment.getId()));
            }
            return ResponseDto.Success("查询成功", articleCommentDtos);
        }
    }

    // 点赞评论
    @ResponseBody
    @GetMapping("/comment/like")
    public ResponseDto doLikeComment(@RequestParam("commentId") Integer commentId, HttpSession session) {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        if (loginUser == null) {
            return ResponseDto.Fail("请登录后进行操作~");
        }
        if (kkBlogService.userLikedArticleComment(loginUser.getId(), commentId)) {
            return ResponseDto.Fail("你已经点过赞咯~");
        }
        ArticleComment articleComment = articleCommentMapper.selectById(commentId);
        ArticleCommentLike commentLike = new ArticleCommentLike();
        commentLike.setCommentId(commentId);
        commentLike.setUserId(loginUser.getId());
        commentLike.setLikedUserId(articleComment.getUserId());
        commentLike.setArticleId(articleComment.getArticleId());
        articleCommentLikeMapper.insert(commentLike);
        articleCommentMapper.addLikeById(commentId);
        return ResponseDto.Success("点赞成功",  articleCommentMapper.selectById(commentId));
    }

    // 取消点赞评论
    @ResponseBody
    @GetMapping("/comment/unlike")
    public ResponseDto doUnlikeComment(@RequestParam("commentId") Integer commentId, HttpSession session) {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        if (loginUser == null) {
            return ResponseDto.Fail("请登录后进行操作~");
        }
        if (!kkBlogService.userLikedArticleComment(loginUser.getId(), commentId)) {
            return ResponseDto.Fail("你已经点过赞咯~");
        }
        kkBlogService.deleteUserLikeArticleComment(loginUser.getId(), commentId);

        articleCommentMapper.cancelLikeById(commentId);
        return ResponseDto.Success("取消点赞成功", articleCommentMapper.selectById(commentId));
    }

    @ResponseBody
    @GetMapping("/delete")
    public ResponseDto deleteArticle(@RequestParam("id") Integer id, HttpSession session) {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        if (loginUser == null) {
            return ResponseDto.Fail("请登录后进行操作");
        }
        Article article = articleMapper.selectById(id);
        if (article == null || !Objects.equals(article.getUserId(), loginUser.getId())) {
            return ResponseDto.Fail("权限验证失败！");
        }
        if (articleMapper.deleteArticleById(id) == 0) {
            return ResponseDto.Fail("操作失败：未找到删除对象");
        }
        return ResponseDto.Success("操作成功", null);
    }

}
