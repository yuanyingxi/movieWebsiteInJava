package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.CreatorMovie;

import com.movie.entity.CreatorMovieResult;
import com.movie.entity.MovieDetailVO;
import com.movie.mapper.CreatorMovieMapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/creatorMovie")
public class CreatorMovieController {
    @Autowired
    private CreatorMovieMapper creatorMovieMapper;


    @GetMapping
    public Result creatorMovie(
     @RequestParam(defaultValue = "1")long creator_id
    ){

        List<CreatorMovie> movies = creatorMovieMapper.findMoviesByCreatorId(creator_id);
        List<MovieDetailVO> movieDetails = new ArrayList<>();
        for (CreatorMovie movie : movies) {
            String movieNo = movie.getMovieNo();
            MovieDetailVO movieDetailVO = creatorMovieMapper.selectMovieDetailsByNo(movieNo);
            movieDetails.add(movieDetailVO);
        }
        long total=movies.size();
        String role=creatorMovieMapper.findSingleRoleByCreatorId(creator_id);
        CreatorMovieResult <CreatorMovie> creatorMovieResult  = new CreatorMovieResult<>(total,role,movies,movieDetails);
        return Result.success(creatorMovieResult);
    }
}
