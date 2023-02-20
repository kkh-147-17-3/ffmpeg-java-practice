package com.shoplive.web.backendtest.service.resize;

import org.springframework.stereotype.Service;

import com.shoplive.web.backendtest.dao.VideoDao;
import com.shoplive.web.backendtest.dto.VideoProgressResponse;
import com.shoplive.web.backendtest.entity.Video;
import com.shoplive.web.backendtest.exception.VideoFileNotFoundException;
import com.shoplive.web.backendtest.util.VideoUploadUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultVideoResizeService implements VideoResizeService {

    private final VideoUploadUtil videoUploadUtil;
    private final int resizedWidth = 360;

    private final VideoDao dao;

    @Override
    public String create(Long videoId, String fileName) {

        return videoUploadUtil.resizeByWidth(videoId, fileName, resizedWidth);

    }

    @Override
    public VideoProgressResponse getProgress(Long videoId) {
        Integer progress =  videoUploadUtil.getProgressMap().get(videoId);
        // 작업 상태로 등록되지 않은 경우, 이미 완료되어 map에서 사라졌을 수 있으니 db에서 기등록여부를 확인한다.
        if (progress == null) {
            Video video = dao.getById(videoId);
            if (video == null) throw new VideoFileNotFoundException("존재하지 않는 ID입니다. id: " + videoId);
            else progress = 100;
        }

        String progressWithPercent = String.valueOf(progress) + "%";
    
        return  VideoProgressResponse.builder()
                                    .id(videoId)
                                    .progress(progressWithPercent)
                                    .build();
    }
}