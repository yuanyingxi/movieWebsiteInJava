package com.movie.entity;

import lombok.Data;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data

public class CreatorMovieResult<T> {
    private long total;
    private String role;
    private List<T> list;
    private List<MovieDetailVO> movieDetails;

    public CreatorMovieResult(long total,String role,List<T> list,List<MovieDetailVO> movieDetails) {
        this.total = total;
        this.role= role;
        this.list = list;
        this.movieDetails = movieDetails;
    }

}
