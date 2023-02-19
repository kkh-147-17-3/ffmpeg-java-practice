package com.shoplive.web.backendtest.service.thumbnail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

import com.shoplive.web.backendtest.exception.ThumbnailUploadException;
import com.shoplive.web.backendtest.util.StorageProperties;
import com.shoplive.web.backendtest.util.VideoUploadUtil;

@Service
public class DefaultVideoThumbnailService implements VideoThumbnailService {

    private final VideoUploadUtil videoUploadUtil;
    private final Path thumbnailLocation;

    public DefaultVideoThumbnailService(StorageProperties properties, VideoUploadUtil util) {
    
        this.thumbnailLocation = Paths.get(util.getThumbnailSavepath()).toAbsolutePath().normalize();
        System.out.println(this.thumbnailLocation);
        try {
            Files.createDirectories(this.thumbnailLocation);
        } catch (Exception ex) {
            throw new ThumbnailUploadException("Could not create the directory where the uploaded files will be stored.");
        }

        this.videoUploadUtil = util;
    }

    @Override
    public void init()
    {
        try
        {
            Files.createDirectory(thumbnailLocation);
        }
        catch (IOException e)
        {
            throw new ThumbnailUploadException ("Could not initialize storage");
        }
    }


    @Override
    public String create(String fileName){

        return videoUploadUtil.createThumbnail(fileName);
        
    }
    
}
