package com.example.kkblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kkblog.domain.Dynamic;
import com.example.kkblog.domain.dto.DynamicDetailDto;
import com.example.kkblog.domain.dto.DynamicPreDto;
import com.example.kkblog.domain.query.DynamicQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaoke
 * @date 2024/4/7
 */
@Mapper
@Repository
public interface DynamicMapper extends BaseMapper<Dynamic> {

    @Select("SELECT a.*,COUNT(c.id) as commentNumber FROM   " +
            "(SELECT d.*,t.`name` as topic_name, u.nickname as nickname, u.avatar as avatar FROM `dynamic` d, `topic` t,`user` u   " +
            "WHERE d.topic_id=t.id and d.user_id=u.id and d.stick = 0 and d.status=0 ORDER BY d.id DESC LIMIT 8) as a  " +
            "LEFT JOIN dynamic_comment c  " +
            "ON a.id = c.dynamic_id  " +
            "GROUP BY a.id " +
            "ORDER BY a.id DESC")
    List<DynamicPreDto> selectNewestDynamics();

    @Select("SELECT a.*,COUNT(c.id) as commentNumber FROM   " +
            "(SELECT d.*,t.`name` as topic_name, u.nickname as nickname, u.avatar as avatar FROM `dynamic` d, `topic` t,`user` u   " +
            "WHERE d.topic_id=t.id and d.user_id=u.id and d.stick = 0 and d.status=0 and d.id < #{id} ORDER BY d.id DESC LIMIT 8) as a  " +
            "LEFT JOIN dynamic_comment c  " +
            "ON a.id = c.dynamic_id  " +
            "GROUP BY a.id " +
            "ORDER BY a.id DESC")
    List<DynamicPreDto> selectMoreNewestDynamics(Integer id);

    @Select("SELECT a.*,COUNT(c.id) as commentNumber FROM  " +
            "(SELECT d.*,t.`name` as topic_name, u.nickname as nickname, u.avatar as avatar FROM `dynamic` d, `topic` t,`user` u  " +
            "WHERE d.topic_id=t.id and d.user_id=u.id and d.status=0 and d.id=#{id}) as a " +
            "LEFT JOIN dynamic_comment c " +
            "ON a.id = c.dynamic_id " +
            "GROUP BY a.id")
    DynamicDetailDto selectDetailById(String id);

    @Select("SELECT a.*,COUNT(c.id) as commentNumber FROM   " +
            "(SELECT d.*,t.`name` as topic_name, u.nickname as nickname, u.avatar as avatar FROM `dynamic` d, `topic` t,`user` u   " +
            "WHERE d.topic_id=t.id and d.user_id=u.id and d.stick = 0 and d.status=0 and d.topic_id=#{id} ORDER BY d.id DESC LIMIT 8) as a  " +
            "LEFT JOIN dynamic_comment c  " +
            "ON a.id = c.dynamic_id  " +
            "GROUP BY a.id " +
            "ORDER BY a.id DESC")
    List<DynamicPreDto> selectDynamicsByTopicId(Integer id);

    @Select("SELECT a.*,COUNT(c.id) as commentNumber FROM   " +
            "(SELECT d.*,t.`name` as topic_name, u.nickname as nickname, u.avatar as avatar FROM `dynamic` d, `topic` t,`user` u   " +
            "WHERE d.topic_id=t.id and d.user_id=u.id and d.stick = 0 and d.status=0 and d.id < #{id} and d.topic_id=#{topicId} ORDER BY d.id DESC LIMIT 8) as a  " +
            "LEFT JOIN dynamic_comment c  " +
            "ON a.id = c.dynamic_id  " +
            "GROUP BY a.id " +
            "ORDER BY a.id DESC")
    List<DynamicPreDto> selectMoreDynamicsByTopicId(Dynamic dynamic);

    @Select("SELECT COUNT(id) FROM `dynamic` WHERE user_id=#{userId}")
    Integer selectDynamicNumberByUserId(Integer userId);

    @Update("UPDATE `dynamic` SET `view_number`=`view_number` + 1 WHERE id=#{id}")
    void addViewById(String id);

    @Update("UPDATE `dynamic` SET `like_number`=`like_number` + 1 WHERE id=#{id}")
    void addLikeById(Integer id);
    @Update("UPDATE `dynamic` SET `like_number`=`like_number` - 1 WHERE id=#{id}")
    void cancelLikeById(Integer id);

    @Update("UPDATE `dynamic` SET `star_number`=`star_number` + 1 WHERE id=#{id}")
    void  addStarById(Integer id);

    @Update("UPDATE `dynamic` SET `star_number`=`star_number` - 1 WHERE id=#{id}")
    void  cancelStarById(Integer id);

    // 根据用户id获取动态
    @Select("SELECT a.*,COUNT(c.id) as commentNumber FROM   " +
            "(SELECT d.*,t.`name` as topic_name, u.nickname as nickname, u.avatar as avatar FROM `dynamic` d, `topic` t,`user` u   " +
            "WHERE d.topic_id=t.id and d.user_id=u.id and d.status=0 and d.user_id=#{id} ORDER BY d.id DESC LIMIT 5) as a  " +
            "LEFT JOIN dynamic_comment c  " +
            "ON a.id = c.dynamic_id  " +
            "GROUP BY a.id " +
            "ORDER BY a.id DESC")
    List<DynamicPreDto> selectByUserId(Integer id);

    // 根据用户id查询更多动态
    @Select("SELECT a.*,COUNT(c.id) as commentNumber FROM   " +
            "(SELECT d.*,t.`name` as topic_name, u.nickname as nickname, u.avatar as avatar FROM `dynamic` d, `topic` t,`user` u   " +
            "WHERE d.topic_id=t.id and d.user_id=u.id and d.status=0 and d.user_id=#{userId} and d.id < #{id} ORDER BY d.id DESC LIMIT 5) as a  " +
            "LEFT JOIN dynamic_comment c  " +
            "ON a.id = c.dynamic_id  " +
            "GROUP BY a.id " +
            "ORDER BY a.id DESC")
    List<DynamicPreDto> selectMoreByUserId(Dynamic dynamic);

    // 删除动态
    @Update("UPDATE `dynamic` SET status=2 WHERE id=#{id}")
    Integer deleteDynamicById(Integer id);

    // 查询推荐动态
    @Select("SELECT a.*,COUNT(c.id) as commentNumber FROM   " +
            "(SELECT d.*,t.`name` as topic_name, u.nickname as nickname, u.avatar as avatar FROM `dynamic` d, `topic` t,`user` u   " +
            "WHERE d.topic_id=t.id and d.user_id=u.id and d.stick = 0 and d.status=0 ORDER BY d.like_number DESC LIMIT 8) as a  " +
            "LEFT JOIN dynamic_comment c  " +
            "ON a.id = c.dynamic_id  " +
            "GROUP BY a.id " +
            "ORDER BY a.like_number DESC")
    List<DynamicPreDto> selectRecommendDynamics();

    @Select("SELECT a.*,COUNT(c.id) as commentNumber FROM   " +
            "(SELECT d.*,t.`name` as topic_name, u.nickname as nickname, u.avatar as avatar FROM `dynamic` d, `topic` t,`user` u   " +
            "WHERE d.topic_id=t.id and d.user_id=u.id and d.stick = 0 and d.status=0 ORDER BY d.like_number DESC LIMIT #{pageNum},8) as a  " +
            "LEFT JOIN dynamic_comment c  " +
            "ON a.id = c.dynamic_id  " +
            "GROUP BY a.id " +
            "ORDER BY a.like_number DESC")
    List<DynamicPreDto> selectMoreRecommendDynamics(Integer pageNum);

    // 查询关注用户的动态
    @Select("SELECT a.*,COUNT(c.id) as commentNumber FROM   " +
            "(SELECT d.*,t.`name` as topic_name, u.nickname as nickname, u.avatar as avatar FROM `dynamic` d, `topic` t,`user` u   " +
            "WHERE d.topic_id=t.id and d.user_id=u.id and d.stick = 0 and d.status=0 and d.user_id in (" +
                "SELECT followed_id FROM `follow` WHERE follower_id=#{userId}" +
            ") ORDER BY d.id DESC LIMIT 8) as a  " +
            "LEFT JOIN dynamic_comment c  " +
            "ON a.id = c.dynamic_id  " +
            "GROUP BY a.id " +
            "ORDER BY a.id DESC")
    List<DynamicPreDto> selectFollowDynamics(Integer userId);

    @Select("SELECT a.*,COUNT(c.id) as commentNumber FROM   " +
            "(SELECT d.*,t.`name` as topic_name, u.nickname as nickname, u.avatar as avatar FROM `dynamic` d, `topic` t,`user` u   " +
            "WHERE d.topic_id=t.id and d.user_id=u.id and d.stick = 0 and d.status=0 and d.user_id in (" +
                "SELECT followed_id FROM `follow` WHERE follower_id=#{userId}" +
            ")  and d.id < #{id} ORDER BY d.id DESC LIMIT 8) as a  " +
            "LEFT JOIN dynamic_comment c  " +
            "ON a.id = c.dynamic_id  " +
            "GROUP BY a.id " +
            "ORDER BY a.id DESC")
    List<DynamicPreDto> selectMoreFollowDynamics(Dynamic dynamic);
}
