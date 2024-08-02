package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.User;
import com.example.kkblog.domain.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Hyh
 * @Date 2024 04 07 20 13
 **/
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM `user` WHERE id=#{id}")
    UserDto selectUserDtoByUserId(Integer id);

    @Select("SELECT * FROM `user` WHERE username=#{username}")
    User selectUserByUsername(String username);

    // 获取注册排名
    @Select("SELECT max(register_rank) + 1 FROM `user`")
    Integer selectRegisterRank();

    // 查询积分排行榜
    @Select("SELECT a.*,count(b.id) AS comment_number FROM " +
            "(SELECT u.*, COUNT(d.id) AS dynamic_number " +
            "FROM `user` u  " +
            "LEFT JOIN dynamic d ON u.id = d.user_id  " +
            "GROUP BY u.id ORDER BY u.scores DESC LIMIT 10) " +
            "as a LEFT JOIN dynamic_comment b ON a.id=b.user_id " +
            "GROUP BY a.id " +
            "ORDER BY a.scores DESC")
    List<UserDto> selectScoresRank();

    // 更新用户主页背景图
    @Update("UPDATE `user` set background_image=#{backgroundImage} WHERE id=#{id}")
    Integer updateUserBgById(User user);

    // 更新用户头像
    @Update("UPDATE `user` set avatar=#{avatar} WHERE id=#{id}")
    Integer updateUserAvatarById(User user);

    // 加积分
    @Update("UPDATE `user` set scores=scores+#{scores} where id=#{id}")
    Integer addUserScores(User user);

    @Select("select * from user where username = #{username};")
    User selectByUserName(String username);

    void addUser(User user);

    //根据username查询所有在线用户
    @Select("select * from user where username != #{username};")
    List<User> selectAllUser(String username);

    //根据username查询用户id
    @Select("select id from user where username = #{username};")
    Integer selectUserId(String username);
}
