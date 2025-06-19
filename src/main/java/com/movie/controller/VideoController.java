package com.movie.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
@RestController
@RequestMapping("/api/video/{videoName}")
public class VideoController {
    @GetMapping
    public ResponseEntity<byte[]> getVideo(@PathVariable String videoName) throws Exception {
        // 1. 构建资源路径（注意路径区分大小写）
        ClassPathResource videoResource = new ClassPathResource("video/" + videoName);

        // 2. 验证文件存在性
        if (!videoResource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 3. 读取二进制数据并设置响应头
        byte[] videoBytes = Files.readAllBytes(videoResource.getFile().toPath());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("video/mp4")) // 正确MIME类型
                .header(HttpHeaders.CONTENT_DISPOSITION)
                .body(videoBytes);
    }
}
