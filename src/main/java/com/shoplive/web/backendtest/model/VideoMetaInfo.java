package com.shoplive.web.backendtest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class VideoMetaInfo {
    private long filesize;
    private int width;
    private int height;
}
