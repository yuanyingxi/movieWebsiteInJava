package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.MovieVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MovieByRegionMapper extends BaseMapper<MovieVO> {
    /**
     * 根据地区查询电影详细信息
     * @param region 电影地区（如：中国大陆、美国）
     * @return 电影信息列表
     */
    @Select("SELECT id, title, url, cover_url, is_vip, average_rating, description, region " +
            "FROM movie " +
            "WHERE region = #{region}")
    List<MovieVO> selectMoviesByRegion(@Param("region") String region);
}
