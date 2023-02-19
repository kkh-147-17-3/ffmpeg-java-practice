package com.shoplive.web.backendtest.service.upload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shoplive.web.backendtest.exception.VideoUploadException;
import com.shoplive.web.backendtest.util.StorageProperties;

@Service
public class DefaultVideoUploadService implements VideoUploadService {

    private final Path rootLocation;
    
    public DefaultVideoUploadService(StorageProperties properties) throws IOException {
    
        this.rootLocation = Paths.get(properties.getVideoUploadDir()).toAbsolutePath().normalize();
        System.out.println(this.rootLocation);
        Files.createDirectories(this.rootLocation);

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

            String ext = FilenameUtils.getExtension(fileName);

            // mp4 확장자가 아닌 경우 예외처리
            if (!ext.equals("mp4")){
                throw new VideoUploadException ("Not Allowed File Extension Type: " + ext);
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
