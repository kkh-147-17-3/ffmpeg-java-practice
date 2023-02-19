package com.shoplive.web.backendtest.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class VideoFileNotFoundException extends RuntimeException{
    
    public VideoFileNotFoundException(String message){
        super(message);
    }
}
