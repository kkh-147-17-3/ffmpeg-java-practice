package com.shoplive.web.backendtest.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shoplive.web.backendtest.dto.VideoUploadRequestDto;
import com.shoplive.web.backendtest.service.VideoService;
import com.shoplive.web.backendtest.service.resize.VideoResizeService;
import com.shoplive.web.backendtest.service.thumbnail.VideoThumbnailService;
import com.shoplive.web.backendtest.service.upload.VideoUploadService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/video")
class VideoUploadController {

    private final VideoService videoService;
    private final VideoUploadService uploadService;
    private final VideoThumbnailService thumbnailService;
    private final VideoResizeService resizeService;

    @GetMapping 
    String getView(){
        return "test";
    }

    @PostMapping (consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<Object> createVideoFile(@RequestPart("metaInfo") VideoUploadRequestDto dto,
                                @RequestPart("videoFile") MultipartFile videoFile){
        String fileName = uploadService.create(videoFile);
        Long videoId = videoService.insert(fileName, dto).getId();
        Thread thread = new Thread(()->{
            try {
                String resizedFileName = resizeService.create(videoId, fileName);
                videoService.updateResizedInfo(videoId, resizedFileName);
                String thumbnailFileName = thumbnailService.create(fileName);
                videoService.updateThumbnailUrl(videoId, thumbnailFileName);

            } catch (Exception e) {
                e.printStackTrace();
                // throw new VideoUploadException("썸네일 생성에 실패했습니다.");
            }
        });
        Map<String,Object> result = new HashMap<>();
        result.put("id", videoId);

        thread.start();
        
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("{id}") 
    @ResponseBody
    public ResponseEntity<Object> getProgress(@PathVariable Long id){
        Integer progress = resizeService.getProgress(id);
        Map<String,Object> result = new HashMap<>();
        result.put("id", id);
        result.put("progress", String.valueOf(progress) + "%");
        return ResponseEntity.ok().body(result);
    }
}