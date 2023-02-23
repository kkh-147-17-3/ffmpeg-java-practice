package com.shoplive.web.backendtest.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VideoProgressResponse {
    private Long id;
    private String progress;
}
