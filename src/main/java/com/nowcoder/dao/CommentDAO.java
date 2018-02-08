package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDAO {
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = "content, user_id, entity_id, entity_Type, created_date, status";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;


    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS, ") values (#{content},#{userId},#{entityId},#{entityType},#{createdDate},#{status})"})
    int addComment(Comment comment);

    @Update({"update ", TABLE_NAME, "set status=#{status} where id=#{id}"})
    void deleteComment(int commentId);

    @Select({"select ", SELECT_FIELDS ," from ", TABLE_NAME, " where entity_type=#{entityType} and entity_id=#{entityId} order by id desc"})
    List<Comment> selectByEntity(@Param("entityType") int entityType, @Param("entityId") int entityId);

    @Select({"select count(id) from ", TABLE_NAME, " where entity_type=#{entityType} and entity_id=#{entityId}"})
    int commentCount(@Param("entityType") int entityType, @Param("entityId") int entityId);

}
