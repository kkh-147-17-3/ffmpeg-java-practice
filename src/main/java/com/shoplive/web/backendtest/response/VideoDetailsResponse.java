package com.shoplive.web.backendtest.response;

import com.shoplive.web.backendtest.model.WebVideoMetaInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VideoDetailsResponse {

    private Long id;
    private String title;
    private WebVideoMetaInfo original;
    private WebVideoMetaInfo resized;
    private String createdAt;
    private String thumbnailUrl;
}
