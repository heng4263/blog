package com.example.kkblog.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

/**
 * @author Hyh
 * @date 2024/4/7
 */
@Data
@TableName("topic")
public class TopicDto {
    private int id;
    // 图标地址
    private String icon;
    // 话题名称
    private String name;
    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    // 是否启用
    private String status;
    // 排序
    private int rank;
    // 是否选中
    private boolean selected = false;
}
