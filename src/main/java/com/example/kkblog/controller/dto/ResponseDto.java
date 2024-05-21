package com.example.kkblog.controller.dto;

/**
 * @Author xiaoke
 * @Date 2024 04 07 23 17
 **/
public class ResponseDto {
    public int code;
    public String msg;

    public Object data;
    public static ResponseDto Success(String msg, Object data) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.code = 200;
        responseDto.msg = msg;
        responseDto.data = data;
        return responseDto;
    }
    public static ResponseDto Fail(String msg) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.code = 400;
        responseDto.msg = msg;
        return responseDto;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
