package com.example.kkblog.config.exception;

import com.example.kkblog.controller.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 全局异常处理
 *
 * @Author xiaoke
 * @Date 2024 04 08 19 30
 **/
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(IllegalAccessException.class)
    @ResponseBody
    public ResponseDto handlerIllegalAccessException(IllegalAccessException e, HttpServletRequest request){
        String url = request.getRequestURI();
        String method = request.getMethod();
        logger.error("请求地址'{}',请求方法'{}'，发生非法获取异常'{}'", url, method, e.getMessage());
        return ResponseDto.Fail(e.getMessage());
    }

    @ExceptionHandler(KKBlogException.class)
    @ResponseBody
    public ResponseDto handlerBusinessException(KKBlogException e,HttpServletRequest request){
        String url = request.getRequestURI();
        String method = request.getMethod();
        logger.error("请求地址'{}',请求方法'{}'，发生业务异常'{}'", url, method, e.getMessage());
        return ResponseDto.Fail(e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
    public ResponseDto handlerIOException(IOException e, HttpServletRequest request){
        String url = request.getRequestURI();
        String method = request.getMethod();
        logger.error("请求地址'{}',请求方法'{}'，发生IO异常'{}'", url, method, e.getMessage());
        return ResponseDto.Fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseDto handlerException(Exception e, HttpServletRequest request){
        String url = request.getRequestURI();
        String method = request.getMethod();
        logger.error("请求地址'{}',请求方法'{}'，发生IO异常'{}'", url, method, e.getMessage());
        return ResponseDto.Fail("系统运行时异常：" + e.getMessage());
    }
}
