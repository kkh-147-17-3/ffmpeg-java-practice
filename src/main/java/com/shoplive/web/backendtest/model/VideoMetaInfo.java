package com.shoplive.web.backendtest.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class VideoMetaInfo {
    private long filesize;
    private int width;
    private int height;
}
