package com.shoplive.web.backendtest.service.Video.Thumbnail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoplive.web.backendtest.Util.StorageProperties;
import com.shoplive.web.backendtest.Util.VideoUploadUtil;
import com.shoplive.web.backendtest.exception.ThumbnailUploadException;
import com.shoplive.web.backendtest.exception.VideoUploadException;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;

@Service
public class DefaultVideoThumbnailService implements VideoThumbnailService {

    private final VideoUploadUtil videoUploadUtil;
    private final Path thumbnailLocation;

    @Autowired
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
        // TODO Auto-generated method stub
        System.out.println(fileName);
        String inputPath = "C:/Users/kterr/uploads/videos/" + fileName;
        String outputPath = "C:/Users/kterr/uploads/thumbs/";
        String ffmpegBasePath = "C:/Users/kterr/Downloads/ffmpeg-5.1.2-essentials_build/bin/";
        FFmpeg ffmpeg = null;
        FFprobe ffprobe = null;
        try {
            ffmpeg = new FFmpeg(ffmpegBasePath+"ffmpeg");
            ffprobe = new FFprobe(ffmpegBasePath+"ffprobe");	
        } catch (IOException e) {
            throw new VideoUploadException("썸네일 생성 과정에러 오류가 발생했습니다.");
        }

        return videoUploadUtil.createThumbnail(fileName);
        
    }
    
}
