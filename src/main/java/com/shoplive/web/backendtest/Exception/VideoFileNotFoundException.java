package com.shoplive.web.backendtest.Exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class VideoFileNotFoundException extends RuntimeException{
    
    public VideoFileNotFoundException(String message){
        super(message);
    }
}
