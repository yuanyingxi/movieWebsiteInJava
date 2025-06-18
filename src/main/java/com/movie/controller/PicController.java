package com.movie.controller;

import com.movie.common.Result;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/movieCover")
public class PicController {
    @GetMapping
    public ResponseEntity<Resource> movieCover(@RequestParam String movieNo) {
        String imagePath = "static/MovieCover/" + movieNo + ".jpg";
        ClassPathResource imgResource = new ClassPathResource(imagePath);

        if (!imgResource.exists()) {
            return ResponseEntity.notFound().build(); // 404 错误
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // 自动识别类型
                .body(imgResource);
    }
    @GetMapping("/creator")
    public ResponseEntity<Resource> creator(
            @RequestParam String movieNo,
            @RequestParam String name) {
        String imagePath = "static/HumanCover/" + movieNo + name + ".jpg";

        ClassPathResource imgResource = new ClassPathResource(imagePath);

        if (!imgResource.exists()) {
            return ResponseEntity.notFound().build(); // 404 错误
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // 自动识别类型
                .body(imgResource);
    }
}
