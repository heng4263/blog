package com.example.kkblog.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author Hyh
 * @Date 2024 04 07 20 12
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDto {
    //来源
    private String source;
    // 动态id
    private Integer dynamicId;
    // 文章id
    private Integer articleId;
    //标题
    private String title;
    //预览
    private String preView;
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
