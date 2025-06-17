package com.movie.mapper;

import com.movie.entity.CreatorMovie;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CreatorMovieMapper {
    @Select("SELECT " +
            "m.movie_no, m.title, m.url, m.cover_url, " +
            "m.is_vip, m.average_rating, " +
            "m.description, m.region " +  // 只选择MovieVO包含的字段
            "FROM movie m " +
            "INNER JOIN movie_creator mc ON m.id = mc.movie_id " +
            "WHERE mc.creator_id = #{creatorId} " +
            "ORDER BY m.release_date DESC")
@Results(id = "movieResultMap1", value = {
        @Result(property = "movieNo", column = "movie_no"),
        @Result(property = "title", column = "title"),
        @Result(property = "url", column = "url"),
        @Result(property = "description", column = "description"),
        @Result(property = "coverUrl", column = "cover_url"),
        @Result(property = "region", column = "region"),
        @Result(property = "isVip", column = "is_vip"),


})
     List<CreatorMovie> findMoviesByCreatorId(Integer creatorId);
}
