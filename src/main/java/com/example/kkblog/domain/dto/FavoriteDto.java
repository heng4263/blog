package com.example.kkblog.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteDto {

    // 收藏用户昵称
    private String nickname;

    private Integer id;

    // 文章/动态标题
    private String title;

    // 预览文字
    @Column(columnDefinition = "LONGTEXT")
    private String preView;

    // 动态/文章内容
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    // 点赞量
    private Integer likeNumber;

    // 收藏量
    private Integer starNumber;

    // 浏览量
    private Integer viewNumber;

    // 发布时间
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    // 标签列表
    private String tags;

    // 动态id
    private Integer dynamicId;

    // 文章id
    private Integer articleId;

    // 判断是文章还是动态
    private Integer type; // 0:动态  1:文章

    // 是否收藏
    @TableField(exist = false)
    private Boolean stared;

}
