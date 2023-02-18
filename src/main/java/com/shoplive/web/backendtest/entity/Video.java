package com.shoplive.web.backendtest.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
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
    private Date createdAt;
    private String thumbnailUrl;
}
