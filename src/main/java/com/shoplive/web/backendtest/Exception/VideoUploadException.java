package com.shoplive.web.backendtest.Exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class VideoUploadException extends RuntimeException {

    public VideoUploadException(String message){
        super(message);
    }
}
