package com.example.kkblog.mapper;

import com.example.kkblog.domain.Search;
import com.example.kkblog.domain.dto.SearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface SearchMapper {
    
//    @Select("SELECT hid, title, type, page_views AS pageViews, " +
//            "TIMESTAMPDIFF(HOUR, create_time, NOW()) AS pastHours, " +
//            "publisher FROM news_headline " +
//            "WHERE is_deleted = 0 " +
//            "<if test='portalVo.keyWords != null and portalVo.keyWords.length() > 0'> " +
//            "AND title LIKE CONCAT('%', #{portalVo.keyWords}, '%') " +
//            "</if> " +
//            "<if test='portalVo.type != 0'> " +
//            "AND type = #{portalVo.type} " +
//            "</if>")
//    List<News> searchNews(@Param("portalVo") PortalVo portalVo);

//    @Select("SELECT id, title, type, view_number AS viewNumber, like_number AS likeNumber, " +
//            "create_time AS pastHours, user_id AS userId,star_number as starNumber " +
//            "FROM ( " +
//            "  SELECT 'dynamic' AS type, id, title, view_number, like_number, create_time, user_id,starNumber " +
//            "  FROM dynamic " +
//            "  WHERE is_deleted = 0 " +
//            "  <if test='search.keyword != null and search.keyword != \"\"'> " +
//            "    AND (title LIKE CONCAT('%', #{search.keyword}, '%') OR dynamic LIKE CONCAT('%', #{search.keyword}, '%')) " +
//            "  </if> " +
//            "  UNION ALL " +
//            "  SELECT 'article' AS type, id, title, view_number, like_number, create_time, user_id,starNumber " +
//            "  FROM article " +
//            "  WHERE is_deleted = 0 " +
//            "  <if test='search.keyword != null and search.keyword != \"\"'> " +
//            "    AND (title LIKE CONCAT('%', #{search.keyword}, '%') OR content LIKE CONCAT('%', #{search.keyword}, '%')) " +
//            "  </if> " +
//            ") AS content " +
//            "WHERE <if test='search.type != 0'>type = #{search.type}</if> " +
//            "ORDER BY create_time DESC")
//    List<SearchDto> searchContent(@Param("search") Search search);

    @Select("SELECT 'dynamic' AS source, id, title, pre_view AS preview, dynamic AS content, " +
            "create_time, user_id, view_number, like_number, star_number " +
            "FROM dynamic " +
            "WHERE dynamic LIKE CONCAT('%', #{keyword}, '%') " +
            "OR title LIKE CONCAT('%', #{keyword}, '%') " +
            "OR tags LIKE CONCAT('%', #{keyword}, '%') " +
            "OR topic_content LIKE CONCAT('%', #{keyword}, '%') " +
            "UNION " +
            "SELECT 'article' AS source, id, title, preview, content, " +
            "create_time, user_id, view_number, like_number, star_number " +
            "FROM article " +
            "WHERE content LIKE CONCAT('%', #{keyword}, '%') " +
            "OR title LIKE CONCAT('%', #{keyword}, '%') " +
            "OR tags LIKE CONCAT('%', #{keyword}, '%') " +
            "OR preview LIKE CONCAT('%', #{keyword}, '%') " +
            "ORDER BY create_time DESC")
    List<Search> search(@Param("keyword") String keyword);

}