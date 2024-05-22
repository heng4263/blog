package com.example.kkblog.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Search {
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
    //用户id
    private Integer userId;
    // 用户昵称
    private String nickname;
    // 标签
    private String tags;


}
