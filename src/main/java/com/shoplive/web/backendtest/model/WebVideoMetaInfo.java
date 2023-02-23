package com.shoplive.web.backendtest.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class WebVideoMetaInfo extends VideoMetaInfo{
    private String videoUrl;
}
