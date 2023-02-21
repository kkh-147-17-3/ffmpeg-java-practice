package com.shoplive.web.backendtest.service.thumbnail;

import java.io.IOException;

public interface VideoThumbnailService {

    String createThumbnail(String fileName) throws IOException;
    
}
