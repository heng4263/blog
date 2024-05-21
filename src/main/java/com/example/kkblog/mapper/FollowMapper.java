package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.Follow;
import com.example.kkblog.domain.dto.UserDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author xiaoke
 * @Date 2024 04 16 22 28
 **/
@Mapper
@Repository
public interface FollowMapper extends BaseMapper<Follow> {
    // 查询用户是否关注另一个用户
    @Select("SELECT COUNT(id) FROM `follow` WHERE follower_id=#{followerId} and followed_id=#{followedId}")
    Integer selectIfFollowing(Follow follow);
    // 取消用户关注
    @Delete("DELETE FROM `follow` WHERE follower_id=#{followerId} and followed_id=#{followedId}")
    Integer cancelFollow(Follow follow);
    // 查询用户的关注用户
    @Select("SELECT u.* from `user` u,follow f " +
            "where u.id=f.followed_id AND f.follower_id=#{id} " +
            "ORDER BY f.id DESC LIMIT 5")
    List<UserDto> selectFollowedUsers(Integer id);
    // 查询用户的粉丝
    @Select("SELECT u.* from `user` u,follow f " +
            "where u.id=f.follower_id AND f.followed_id=#{id} " +
            "ORDER BY f.id LIMIT 5")
    List<UserDto> selectFans(Integer id);

    // 查询用户的关注用户id列表
    @Select("SELECT followed_id FROM follow WHERE follower_id=#{id}")
    List<Integer> selectFollowedIds(Integer id);
    // 查询用户的粉丝数量
    @Select("SELECT COUNT(*) FROM follow WHERE followed_id=#{id}")
    Integer selectFanNumber(Integer id);
    // 查询用户的关注数量
    @Select("SELECT COUNT(*) FROM follow WHERE follower_id=#{id}")
    Integer selectFollowNumber(Integer id);
}
