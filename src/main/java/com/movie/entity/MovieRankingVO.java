package com.movie.entity;

import lombok.Data;

@Data
public class MovieRankingVO {
    private String id;
    private String title;
    private Integer playCount;
    private Double rating;
    private String date;
    private Integer rank;

    // 无参构造器
    public MovieRankingVO() {}

    // 有参构造器
    public MovieRankingVO(String id ,String title, Integer playCount, Double rating, String date) {
        this.id = id;
        this.title = title;
        this.playCount = playCount;
        this.rating = rating;
        this.date = date;
    }
}