package com.shoplive.web.backendtest.helper;

import org.springframework.stereotype.Component;

import com.shoplive.web.backendtest.model.VideoMetaInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FfmpegVideoUploadHelper extends VideoUploadHelper{

    private final FfmpegVideoHandler ffmpegVideoHandler;

    @Override
    public VideoMetaInfo getMataInfoByFileName(String fileName) {
        String filePath = this.getOriginPath() + fileName;
        return ffmpegVideoHandler.getMetaInfoFromProbeResult(filePath);
    }
    
}
