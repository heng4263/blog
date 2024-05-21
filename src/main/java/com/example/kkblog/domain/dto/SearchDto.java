package com.example.kkblog.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author xiaoke
 * @Date 2024 04 07 20 12
 **/
@Data
public class SearchDto {
    //来源
    private String source;
    //id
    private Integer id;
    //标题
    private String title;
    //预览
    private String preView;
    //内容
    private String content;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    //用户
    private Integer userId;
    //浏览量
    private Integer viewNumber;
    //点赞量
    private Integer likeNumber;
    //收藏量
    private Integer starNumber;
}
