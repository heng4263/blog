package com.example.kkblog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.kkblog.config.exception.KKBlogException;
import com.example.kkblog.controller.dto.ResponseDto;
import com.example.kkblog.domain.Dynamic;
import com.example.kkblog.domain.dto.TopicDto;
import com.example.kkblog.domain.dto.UserDto;
import com.example.kkblog.mapper.DynamicMapper;
import com.example.kkblog.mapper.TopicMapper;
import com.example.kkblog.service.KKBlogService;
import com.example.kkblog.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Hyh
 * @date 2024/4/22
 */
@Controller
@RequestMapping("/edit")
public class editController {
    @Autowired
    private DynamicMapper dynamicMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private KKBlogService kkBlogService;

    @GetMapping("/create")
    public String createEdit(@RequestParam("id") Integer id, HttpSession session, Model model) {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        if (loginUser == null) {
            return "error/403";
        }
        Dynamic dynamic = dynamicMapper.selectById(id);
        if (dynamic == null) {
            return "error/404";
        }
        if (!Objects.equals(dynamic.getUserId(), loginUser.getId()) || dynamic.getStatus() != 0) {
            return "error/403";
        }
        model.addAttribute("dynamic", dynamic);
        List<TopicDto> topicDtos = topicMapper.selectAvailableTopics();
        String filterTopics = "最新,推荐,关注";
        model.addAttribute("topics", topicDtos.stream().filter(i -> !filterTopics.contains(i.getName())).collect(Collectors.toList()));
        if (0 == dynamic.getType()) {
            return "edit/create";
        }
        return "edit/topic-create";
    }

    // 更新动态
    @ResponseBody
    @PostMapping("/post")
    public ResponseDto postDynamic(Dynamic dynamic, HttpSession session) throws KKBlogException {
        Dynamic updateDynamic = dynamicMapper.selectById(dynamic.getId());
        StringUtils.trimObject(dynamic);
        if (dynamic.getTopicId() == null) {
            return ResponseDto.Fail("请选择话题！");
        }
        if (updateDynamic.getType() == 0) {
            if (StringUtils.isEmpty(dynamic.getDynamic())) {
                return ResponseDto.Fail("请输入您想分享的动态内容！");
            }
            // 设置预览文字
            if (dynamic.getDynamic().length() > 200) {
                dynamic.setPreView(dynamic.getDynamic().substring(0, 200) + "...");
            } else {
                dynamic.setPreView(dynamic.getDynamic());
            }
        } else if (updateDynamic.getType() == 1) {
            if (StringUtils.isEmpty(dynamic.getTitle())) {
                return ResponseDto.Fail("请输入话题的标题！");
            }
            // 设置预览文字
            dynamic.setPreView(dynamic.getPreView().replaceAll("<", "《").replaceAll(">", "》"));
            if (dynamic.getPreView().length() > 200) {
                dynamic.setPreView(dynamic.getPreView().substring(0, 200) + "...");
            }
        }
        // 获取ip地址
        UserDto loginUser = (UserDto) session.getAttribute("user");
        String tagString = dynamic.getTags();
        if (!StringUtils.isEmpty(tagString)) {
            JSONArray tags = JSON.parseArray(tagString);
            for (Object tag : tags) {
                kkBlogService.quoteATag((String) tag);
            }
        }
        // 设置状态
        updateDynamic.setStatus(0);
        // 更新数据库
        updateDynamic.setTopicId(dynamic.getTopicId());
        updateDynamic.setDynamic(dynamic.getDynamic());
        updateDynamic.setTitle(dynamic.getTitle());
        updateDynamic.setTopicContent(dynamic.getTopicContent());
        updateDynamic.setIpLocation(loginUser.getIpLocation());
        updateDynamic.setImageList(dynamic.getImageList());
        updateDynamic.setTags(dynamic.getTags());
        updateDynamic.setPreView(dynamic.getPreView());
        dynamicMapper.updateById(updateDynamic);
        return ResponseDto.Success("更新成功！", updateDynamic);
    }
}
