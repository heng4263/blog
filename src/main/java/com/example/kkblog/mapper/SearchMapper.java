package com.example.kkblog.mapper;

import com.example.kkblog.domain.Search;
import com.example.kkblog.domain.dto.SearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface SearchMapper {
    @Select("SELECT source, id, title, preview, content, tags, create_time, user_id, user_nickname " +
            "FROM ( " +
            "    SELECT 'dynamic' AS source, d.id, d.title, d.pre_view AS preview, d.dynamic AS content, d.tags, d.create_time, d.user_id, u.nickname AS user_nickname " +
            "    FROM dynamic d " +
            "    LEFT JOIN user u ON d.user_id = u.id " +
            "    WHERE d.dynamic LIKE CONCAT('%', #{keyword}, '%') " +
            "       OR d.title LIKE CONCAT('%', #{keyword}, '%') " +
            "       OR d.tags LIKE CONCAT('%', #{keyword}, '%') " +
            "       OR d.topic_content LIKE CONCAT('%', #{keyword}, '%') " +
            "       OR u.nickname LIKE CONCAT('%', #{keyword}, '%') " +
            "    UNION ALL " +
            "    SELECT 'article' AS source, a.id, a.title, a.preview, a.content, a.tags, a.create_time, a.user_id, u.nickname AS user_nickname " +
            "    FROM article a " +
            "    LEFT JOIN user u ON a.user_id = u.id " +
            "    WHERE a.content LIKE CONCAT('%', #{keyword}, '%') " +
            "       OR a.title LIKE CONCAT('%', #{keyword}, '%') " +
            "       OR a.tags LIKE CONCAT('%', #{keyword}, '%') " +
            "       OR a.preview LIKE CONCAT('%', #{keyword}, '%') " +
            "       OR u.nickname LIKE CONCAT('%', #{keyword}, '%') " +
            ") AS CombinedResults " +
            "ORDER BY create_time DESC")
    List<Search> search(@Param("keyword") String keyword);

    @Select("SELECT * FROM (" +
            "    SELECT 'dynamic' AS source, d.id, d.title, d.pre_view AS preview, d.dynamic AS content, d.tags, d.create_time, d.user_id, u.nickname AS user_nickname " +
            "    FROM dynamic d " +
            "    LEFT JOIN user u ON d.user_id = u.id " +
            "    WHERE (" +
            "            d.dynamic LIKE CONCAT('%', #{keyword}, '%') " +
            "        OR d.title LIKE CONCAT('%', #{keyword}, '%') " +
            "        OR d.topic_content LIKE CONCAT('%', #{keyword}, '%') " +
            "        OR u.nickname LIKE CONCAT('%', #{keyword}, '%') " +
            "    ) " +
            "    AND JSON_CONTAINS(d.tags, #{tag}) " +
            "    UNION ALL " +
            "    SELECT 'article' AS source, a.id, a.title, a.preview, a.content, a.tags, a.create_time, a.user_id, u.nickname AS user_nickname " +
            "    FROM article a " +
            "    LEFT JOIN user u ON a.user_id = u.id " +
            "    WHERE (" +
            "            a.content LIKE CONCAT('%', #{keyword}, '%') " +
            "        OR a.title LIKE CONCAT('%', #{keyword}, '%') " +
            "        OR a.preview LIKE CONCAT('%', #{keyword}, '%') " +
            "        OR u.nickname LIKE CONCAT('%', #{keyword}, '%') " +
            "    ) " +
            "    AND JSON_CONTAINS(a.tags, #{tag}) " +
            ") AS CombinedResults " +
            "ORDER BY create_time DESC")
    List<Search> searchByKeywordAndNodeId(@Param("keyword") String keyword, @Param("tag") String tag);

}