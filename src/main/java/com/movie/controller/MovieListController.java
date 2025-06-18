package com.movie.controller;

import com.movie.common.Result;
import com.movie.common.annotation.AccessLimit;
import com.movie.entity.MovieVO;
import com.movie.entity.PageResult;
import com.movie.mapper.MovieByRegionMapper;
import com.movie.mapper.MovieByTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/movie/list")
public class MovieListController {
    @Autowired
    private MovieByTypeMapper movieByTypeMapper;
    @Autowired
    private MovieByRegionMapper movieByRegionMapper;

    @AccessLimit(seconds = 5, maxCount = 3, needLogin = false)
    @GetMapping("/type")
    public Result MovieType(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam String type
    ){
//        获取movie list数据
        Integer typeId = movieByTypeMapper.findIdByTypename(type);
        List<Long> l = movieByTypeMapper.findMovieIdsByGenreId(typeId);
        List<MovieVO> data = new java.util.ArrayList<>(List.of());
        for(int i=0;i<l.size();i++){
            MovieVO m = movieByTypeMapper.selectMovieById(l.get(i));

            data.add(m);
        }

        int total = data.size();
        int start = (page - 1) * size;
        int end = Math.min(start + size, total);


        List<MovieVO> pageList = data.subList(start, end);

        PageResult<MovieVO> pageResult = new PageResult<>(
            pageList, total, page, size
        );
        return Result.success(pageResult);
    }

    @GetMapping("/region")
    public Result MovieRegion(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam String region
    ){

        //获取电影数据列表
        List<MovieVO> data1 =movieByRegionMapper.selectMoviesByRegion(region);


        int total = data1.size();
        int start = (page - 1) * size;
        int end = Math.min(start + size, total);

        List<MovieVO> pageList = data1.subList(start, end);

        PageResult<MovieVO> pageResult1 = new PageResult<>(
                pageList, total, page, size
        );
        return Result.success(pageResult1);
    }

}
