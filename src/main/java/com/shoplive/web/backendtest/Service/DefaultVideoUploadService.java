package com.shoplive.web.backendtest.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shoplive.web.backendtest.Exception.VideoUploadException;
import com.shoplive.web.backendtest.Util.StorageProperties;

@Service
public class DefaultVideoUploadService implements VideoUploadService {

    private final Path rootLocation;

    @Autowired
    public DefaultVideoUploadService(StorageProperties properties) {
    
        this.rootLocation = Paths.get(properties.getUploadDir()).toAbsolutePath().normalize();
        System.out.println(this.rootLocation);
        try {
            Files.createDirectories(this.rootLocation);
        } catch (Exception ex) {
            throw new VideoUploadException("Could not create the directory where the uploaded files will be stored.");
        }
    }

    @Override
    public void init()
    {
        try
        {
            Files.createDirectory(rootLocation);
        }
        catch (IOException e)
        {
            throw new VideoUploadException ("Could not initialize storage");
        }
    }


    @Override
    public String create(MultipartFile videoFile) {
        
        // Normalize file name
        String fileName = StringUtils.cleanPath(videoFile.getOriginalFilename());
        try
        {
            if (videoFile.isEmpty())
            {
                throw new VideoUploadException ("Failed to store empty file " + videoFile.getOriginalFilename());
            }
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.rootLocation.resolve(fileName);
            Files.copy(videoFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }
        catch (IOException e)
        {
            throw new VideoUploadException ("Failed to store file " + videoFile.getOriginalFilename());
        }
        
    }
    
}
