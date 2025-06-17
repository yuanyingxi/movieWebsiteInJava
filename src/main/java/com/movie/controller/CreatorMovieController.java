package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.CreatorMovie;

import com.movie.mapper.CreatorMovieMapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creatorMovie")
public class CreatorMovieController {
    @Autowired
    private CreatorMovieMapper creatorMovieMapper;


    @GetMapping
    public Result creatorMovie(
     @RequestParam(defaultValue = "1")Integer creator_id

    ){

        List<CreatorMovie> movies = creatorMovieMapper.findMoviesByCreatorId(creator_id);

        return Result.success(movies);
    }
}
