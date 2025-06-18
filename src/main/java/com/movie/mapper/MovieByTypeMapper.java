package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.MovieVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MovieByTypeMapper extends BaseMapper<MovieVO> {
    /**
     * 根据类型名称查询对应的类型ID
     * @param typename 类型名称（genre.name字段）
     * @return 类型ID（genre.id），未找到时返回null
     */
    @Select("SELECT id FROM genre WHERE name = #{typename}")
    Integer findIdByTypename(@Param("typename") String typename);

    /**
     * 根据电影类型ID查询关联的电影ID列表
     * 注意：多对多关系中，一个类型可能关联多个电影
     *
     * @param genreId 电影类型ID (关联 genre.id)
     * @return 关联的电影ID集合 (movie.id 列表)，若无结果返回空集合
     */
    @Select("SELECT movie_id FROM movie_genre WHERE genre_id = #{genreId} ORDER BY movie_id")
    List<Long> findMovieIdsByGenreId(@Param("genreId") int genreId);

//    /**
//     * 根据电影ID查询电影详细信息
//     *
//     * @param id 电影ID (对应 movie.id)
//     * return 电影实体对象，未找到时返回 null
//     */
    @Select("SELECT * FROM movie WHERE id = #{id}")

    @Results(id = "movieResultMap", value = {
            @Result(property = "id",column = "movie_no"),
            @Result(property = "title", column = "title"),
            @Result(property = "url", column = "url"),
            @Result(property = "coverUrl", column = "cover_url"),
            @Result(property = "isVip", column = "is_vip"),
            @Result(property = "averageRating", column = "average_rating"),
            @Result(property = "description",column = "description"),
            @Result(property="region",column="region"),

    })
     MovieVO selectMovieById(@Param("id") Long id);

}