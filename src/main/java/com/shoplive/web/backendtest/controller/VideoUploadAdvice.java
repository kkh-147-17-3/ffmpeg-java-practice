package com.shoplive.web.backendtest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.shoplive.web.backendtest.exception.ThumbnailUploadException;
import com.shoplive.web.backendtest.exception.VideoUploadException;

@ControllerAdvice
public class VideoUploadAdvice {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResponseEntity<String> maxUploadSizeExceededExceptionHandler(Exception e){
        return new ResponseEntity<>("업로드하신 파일의 사이즈가 100MB를 초과합니다.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(VideoUploadException.class)
    @ResponseBody
    public ResponseEntity<String> videoUploadExceptionHandler(Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ThumbnailUploadException.class)
    @ResponseBody
    public ResponseEntity<String> thumbnailUploadExceptionHandler(Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
}
