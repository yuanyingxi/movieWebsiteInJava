package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.MovieVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MovieByRegionMapper extends BaseMapper<MovieVO> {
    /**
     * 根据地区查询电影详细信息
     * @param region 电影地区（如：中国大陆、美国）
     * @return 电影信息列表
     */
    @Select("SELECT * FROM movie WHERE region = #{region}")
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
    List<MovieVO> selectMoviesByRegion(@Param("region") String region);
}
