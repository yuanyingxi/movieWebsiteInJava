package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.MovieRankingVO;
import com.movie.mapper.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rank")
public class RankController {
    @Autowired
    private MovieMapper movieMapper;

    @GetMapping("/list")
    public Result selectRanking(@RequestParam("rankType") int rankType) {
        List<MovieRankingVO> result;
        switch (rankType) {
            case 1:
                result = movieMapper.selectPlayWeek();
                break;
            case 2:
                result = movieMapper.selectPlayMonth();
                break;
            case 3:
                result = movieMapper.selectPlayAll();
                break;
            case 4:
                result = movieMapper.selectByRate();
                break;
            case 5:
                result = movieMapper.selectByHot();
                break;
            default:
                return Result.error("无效的排名类型");
        }
        return Result.success(result);
    }
}
