package com.movie.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("movie")
public class MovieVO {
    private Integer id;
    private String title;
    private String url;
    private String coverUrl;
    private Integer isVip;
    private Double averageRating;
    private String description;
    private String region;

}
