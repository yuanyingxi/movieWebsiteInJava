package com.movie.mapper;

import com.movie.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface MovieMapper extends BaseMapper<Movie> {
    // Chart1：统计各类型电影数量
    @Select("SELECT g.name AS name, COUNT(mg.movie_id) AS value FROM movie_genre mg JOIN genre g ON mg.genre_id = g.id GROUP BY g.name")
    List<Genre> selectGenreStats();

    // Chart2：查询播放量TOP10电影
    @Select("SELECT title, play_count AS playCount FROM movie ORDER BY play_count DESC LIMIT 10")
    List<MovieRankingVO> selectPlayTop10();

    @Results(id = "rankingMap", value = {
            @Result(column = "title", property = "title"),
            @Result(column = "play_count", property = "playCount")
    })
    @Select("SELECT title, play_count FROM movie ORDER BY play_count DESC LIMIT 10")
    List<MovieRankingVO> selectPlayRankings();

    @Select("<script>" +
            "SELECT movie_no AS id, title, url, cover_url, is_vip, average_rating, description FROM movie" +
            "<where>" +
            "   <choose>" +
            "       <when test='isVip == 0'>" +
            "           AND is_vip = 0" +
            "       </when>" +
            "       <when test='isVip == 1'>" +
            "           AND is_vip = 1" +
            "       </when>" +
            "       <otherwise>" +
            "           AND (is_vip = 0 OR is_vip = 1)" +
            "       </otherwise>" +
            "   </choose>" +
            "</where>" +
            "</script>")
    @Results({
            @Result(property = "movie_no", column = "movie_no"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "url", column = "url"),
            @Result(property = "coverUrl", column = "cover_url"),
            @Result(property = "isVip", column = "is_vip"),
            @Result(property = "averageRating", column = "average_rating")
    })
    List<MovieBasicVO> selectMovieList(@Param("isVip") int isVip);

    @Select("SELECT movie_no AS id, title, play_count AS playCount, average_rating as rating, release_date as date FROM movie ORDER BY play_count DESC LIMIT 19,20")
    List<MovieRankingVO> selectPlayWeek();

    @Select("SELECT movie_no AS id, title, play_count AS playCount, average_rating as rating, release_date as date FROM movie ORDER BY play_count DESC LIMIT 29,20")
    List<MovieRankingVO> selectPlayMonth();

    @Select("SELECT movie_no AS id, title, play_count AS playCount, average_rating as rating, release_date as date FROM movie ORDER BY play_count DESC LIMIT 50")
    List<MovieRankingVO> selectPlayAll();

    @Select("SELECT movie_no AS id, title, play_count AS playCount, average_rating as rating, release_date as date FROM movie ORDER BY average_rating DESC LIMIT 20")
    List<MovieRankingVO> selectByRate();

    @Select("SELECT movie_no AS id, title, play_count AS playCount, average_rating as rating, release_date as date FROM movie ORDER BY rating_count DESC LIMIT 20")
    List<MovieRankingVO> selectByHot();


    /**
     * 根据电影编号获取主创人员列表（导演排第一位）
     * @param movieNo 电影编号
     * @return 主创人员姓名列表
     */
    @Select("SELECT c.name " +
            "FROM creator c " +
            "JOIN movie_creator mc ON c.id = mc.creator_id " +
            "JOIN movie m ON mc.movie_id = m.id " +
            "WHERE m.movie_no = #{movieNo} " +
            "ORDER BY CASE WHEN mc.role_id = 1 THEN 0 ELSE 1 END, c.name")
    List<String> selectCreatorsByMovieNo(@Param("movieNo") String movieNo);
}
