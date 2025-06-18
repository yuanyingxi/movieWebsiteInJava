package com.movie.entity;

import lombok.Data;


import java.util.List;
@Data

public class CreatorMovieResult<T> {
    private long total;
    private String role;
    private List<T> list;

    public CreatorMovieResult(long total,String role,List<T> list) {
        this.total = total;
        this.role= role;
        this.list = list;
    }

}
