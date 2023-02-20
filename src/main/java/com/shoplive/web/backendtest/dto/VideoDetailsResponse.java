package com.shoplive.web.backendtest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoDetailsResponse {

    private Long id;
    private String title;
    private VideoMetaInfo original;
    private VideoMetaInfo resized;
    private String createdAt;
    private String thumbnailUrl;
}
