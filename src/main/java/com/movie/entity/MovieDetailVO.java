package com.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetailVO {
    private List<String> genres; // 类型列表，如["爱情", "动作"]
    private Double averageRating; // 平均评分
    private Date releaseDate; // 上映日期

}