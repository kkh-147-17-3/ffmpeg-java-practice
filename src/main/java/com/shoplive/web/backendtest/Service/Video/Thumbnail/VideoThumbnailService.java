package com.shoplive.web.backendtest.service.Video.Thumbnail;

import java.io.IOException;

public interface VideoThumbnailService {

    void init();

    String create(String fileName) throws IOException;
    
}
