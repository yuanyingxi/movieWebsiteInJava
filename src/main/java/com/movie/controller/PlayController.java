package com.movie.controller;

import com.movie.common.Result;
import com.movie.service.PlayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movie")
public class PlayController {

    @Autowired
    private PlayService playService;

    @GetMapping("/play/{movieId}")
    public Result play(@PathVariable String movieId, HttpServletRequest request) {
        System.out.println(movieId);
        return playService.getMovie(movieId, request);
    }

}
