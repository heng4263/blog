package com.example.kkblog.config.exception;

import lombok.Getter;

/**
 * 自定义全局异常
 * @Author Hyh
 * @Date 2024 04 08 19 34
 **/
@Getter
public class KKBlogException extends RuntimeException{
    private static final long serialVersionUID = -7480022450501760611L;

    /**
     * 异常提示信息
     */
    private String message;

    public KKBlogException(String msg) {
        this.message = msg;
    }
    // get set方法

    public void setMessage(String message) {
        this.message = message;
    }


}
