package com.shoplive.web.backendtest.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class VideoProgressResponse {
    private Long id;
    private String progress;
}
