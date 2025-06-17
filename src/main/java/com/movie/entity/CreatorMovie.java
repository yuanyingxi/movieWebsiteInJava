package com.movie.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("movie")
public class CreatorMovie {
    private String movieNo;  // 电影编号
    private String title;  // 电影标题
    private String url;    // 电影url
    private String coverUrl;  // 电影海报URL
    private Integer isVip;  // 是否会员
    private BigDecimal averageRating;  // 平均评分
    private String description;  // 剧情简介
    private String region;  // 上映地区
}
