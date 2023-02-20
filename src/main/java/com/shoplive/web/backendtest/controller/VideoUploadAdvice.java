package com.shoplive.web.backendtest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class VideoUploadAdvice {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResponseEntity<String> maxUploadSizeExceededExceptionHandler(Exception e){
        System.out.println("파일 사이즈 큼");
        return new ResponseEntity<>("파일 사이즈 큼", HttpStatus.FORBIDDEN);
    }
}
