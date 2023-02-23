package com.shoplive.web.backendtest.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Video {
    private Long id;
    private String title;
    private long originalFilesize;
    private int originalWidth;
    private int originalHeight;
    private String originalVideoUrl;
    private long resizedFilesize;
    private int resizedWidth;
    private int resizedHeight;
    private String resizedVideoUrl;
    private LocalDateTime createdAt;
    private String thumbnailUrl;
}
