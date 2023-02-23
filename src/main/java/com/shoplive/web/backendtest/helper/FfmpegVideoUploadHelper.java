package com.shoplive.web.backendtest.helper;

import org.springframework.stereotype.Component;

import com.shoplive.web.backendtest.model.VideoMetaInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FfmpegVideoUploadHelper extends VideoUploadHelper{

    private final FfmpegVideoHandler ffmpegVideoHandler;

    @Override
    public VideoMetaInfo getMetaInfoByFileName(String fileName) {
        String filePath = this.originPath + fileName;
        return ffmpegVideoHandler.getMetaInfoFromProbeResult(filePath);
    }
    
}
