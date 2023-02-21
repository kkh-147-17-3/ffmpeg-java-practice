package com.shoplive.web.backendtest.Response;

import com.shoplive.web.backendtest.model.WebVideoMetaInfo;

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
    private WebVideoMetaInfo original;
    private WebVideoMetaInfo resized;
    private String createdAt;
    private String thumbnailUrl;
}
