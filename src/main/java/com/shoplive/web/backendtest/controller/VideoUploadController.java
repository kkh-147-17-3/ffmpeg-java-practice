package com.shoplive.web.backendtest.controller;

import java.io.IOException;
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

import com.shoplive.web.backendtest.exception.VideoUploadException;
import com.shoplive.web.backendtest.request.VideoUploadRequest;
import com.shoplive.web.backendtest.response.VideoDetailsResponse;
import com.shoplive.web.backendtest.response.VideoProgressResponse;
import com.shoplive.web.backendtest.service.VideoService;
import com.shoplive.web.backendtest.service.resize.VideoResizeService;
import com.shoplive.web.backendtest.service.thumbnail.VideoThumbnailService;
import com.shoplive.web.backendtest.service.upload.VideoUploadService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/video")
public class VideoUploadController {

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
    public ResponseEntity<Object> createVideoFile(@RequestPart("metaInfo") VideoUploadRequest dto,
                                @RequestPart("videoFile") MultipartFile videoFile){
        String fileName = uploadService.create(videoFile);
        Long videoId = videoService.insert(fileName, dto).getId();
        Thread thread = new Thread(()->{
            try {
                String thumbnailFileName = thumbnailService.createThumbnail(fileName);
                videoService.updateThumbnailUrl(videoId, thumbnailFileName);
                
                String resizedFileName = resizeService.createResized(fileName);
                videoService.updateResizedInfo(videoId, resizedFileName);
                
            } catch (IOException e){
                throw new VideoUploadException("썸네일 생성에 실패했습니다.");
            }
        });
        thread.start();
        
        Map<String,Object> result = new HashMap<>();
        result.put("id", videoId);
        
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("{id}") 
    @ResponseBody
    public ResponseEntity<Object> getDetails(@PathVariable Long id){
        VideoDetailsResponse response = videoService.getDetails(id);
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("{id}/progress") 
    @ResponseBody
    public ResponseEntity<Object> getResizeProgress(@PathVariable Long id){
        VideoProgressResponse response = resizeService.getProgress(id);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("{id}/thumbnail/progress") 
    @ResponseBody
    public ResponseEntity<Object> getThumbnailProgress(@PathVariable Long id){
        VideoProgressResponse response = thumbnailService.getProgress(id);

        return ResponseEntity.ok().body(response);
    }
}