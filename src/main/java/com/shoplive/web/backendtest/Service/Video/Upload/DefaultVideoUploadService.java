package com.shoplive.web.backendtest.service.Video.Upload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shoplive.web.backendtest.Util.StorageProperties;
import com.shoplive.web.backendtest.Util.VideoUploadUtil;
import com.shoplive.web.backendtest.dto.VideoUploadRequestDto;
import com.shoplive.web.backendtest.exception.VideoUploadException;

import net.bramp.ffmpeg.FFprobe;

@Service
public class DefaultVideoUploadService implements VideoUploadService {

    private final Path rootLocation;

    @Autowired
    public DefaultVideoUploadService(StorageProperties properties) {
    
        this.rootLocation = Paths.get(properties.getVideoUploadDir()).toAbsolutePath().normalize();
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
