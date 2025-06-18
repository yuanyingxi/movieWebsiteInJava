package com.movie.mapper;

import com.movie.entity.CreatorMovie;
import com.movie.entity.MovieDetailVO;
import org.apache.ibatis.annotations.*;

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
    List<CreatorMovie> findMoviesByCreatorId(long creatorId);

    @Select("SELECT m.id, m.average_rating as averageRating, m.release_date as releaseDate " +
            "FROM movie m WHERE m.movie_no = #{movieNo}")
    @Results({
            @Result(property = "genres", column = "id",
                    many = @Many(select = "getGenresByMovieId")),
            @Result(property = "averageRating", column = "averageRating"),
            @Result(property = "releaseDate", column = "releaseDate")
    })
    MovieDetailVO selectMovieDetailsByNo(@Param("movieNo") String movieNo);

    @Select("SELECT g.name FROM genre g " +
            "JOIN movie_genre mg ON g.id = mg.genre_id " +
            "WHERE mg.movie_id = #{movieId}")
    List<String> getGenresByMovieId(@Param("movieId") Long movieId);


    /**
    * 查询主创最常担任的角色类型（单个字符串）
            * @param creatorId 主创ID
     * @return 最高频角色名称（如 "导演"），无结果时返回 null
            */
    @Select("""
        SELECT r.name
        FROM movie_creator mc
        JOIN role r ON mc.role_id = r.id
        WHERE mc.creator_id = #{creatorId}
        LIMIT 1  -- 确保只返回一个结果
    """)
    @Results(id = "roleResultMap", value = {
            @Result(property = "roleName", column = "name", javaType = String.class)  // 精确映射[1,2](@ref)
    })
    String findSingleRoleByCreatorId(@Param("creatorId") long creatorId);
}
