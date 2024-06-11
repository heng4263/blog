package com.example.kkblog.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserLinkMapper {
    @Select(" select linkId from user_link where `from` = #{fromId} and `to` = #{toId}")
    Integer selectLinkId(int fromId,int toId);

    @Insert(" insert into user_link(`from`,`to`) values(#{min},#{max})")
    void insertLink(int min, int max);
}