package com.example.kkblog.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Search {
    private String keyword;
    private int type; // 0: 全部, 1: 动态, 2: 文章

}
