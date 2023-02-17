package com.shoplive.web.backendtest.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shoplive.web.backendtest.Service.VideoUploadService;

import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/video")
class VideoUploadController {

    private final VideoUploadService uploadService;

    @GetMapping 
    String getView(){
        return "test";
    }

    @PostMapping
    @ResponseBody
    public void createVideoFile(@RequestPart @Nullable MultipartFile videoFile){
        System.out.println(videoFile);
        System.out.println("요청받음");
        uploadService.create(videoFile);
    }
    
}