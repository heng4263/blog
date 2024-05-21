package com.example.kkblog.enmu;

public enum FAVORITE_TYPE {
    ARTICLE("文章"),
    DYNAMIC("动态");

    private String description;

    FAVORITE_TYPE(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
