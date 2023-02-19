package com.shoplive.web.backendtest.service.thumbnail;

import java.io.IOException;

public interface VideoThumbnailService {

    void init();

    String create(String fileName) throws IOException;
    
}
