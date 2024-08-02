package com.example.kkblog.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * @Author Hyh
 * @Date 2024 04 17 23 17
 **/
@Data
@TableName("ver_code")
@Entity
public class VerCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "int(11) NOT NULL AUTO_INCREMENT")
    private Integer id;

    private String codeKey;

    private String codeValue;
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
}
