package com.shoplive.web.backendtest.helper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.shoplive.web.backendtest.exception.ThumbnailUploadException;
import com.shoplive.web.backendtest.interfaces.ProgressibleVideoResizer;
import com.shoplive.web.backendtest.interfaces.ProgressibleVideoThumbnailer;
import com.shoplive.web.backendtest.model.VideoMetaInfo;
import com.shoplive.web.backendtest.model.WebVideoMetaInfo;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;


@ConfigurationProperties(prefix="video")
@Getter
@Setter
public abstract class VideoUploadHelper {

    private String originPath;
    private String videoUrl;

    @Autowired
    private ProgressibleVideoResizer videoResizer;

    @Autowired
    private ProgressibleVideoThumbnailer videoThumbnailer;

    private String resultThumbnailExt = ".gif";
    private String thumbnailSavepath;
    private String thumbnailSuffix;
    private String thumbnailUrl;
    private String convertSavePath;

    @PostConstruct
    public void init123(){
        System.out.println("init: "+ convertSavePath);
        System.out.println("init: " + thumbnailSavepath);
        Path resizedLocation = Paths.get(convertSavePath).toAbsolutePath().normalize();
        Path thumbnailLocation = Paths.get(thumbnailSavepath).toAbsolutePath().normalize();
        try {
            if(!Files.exists(thumbnailLocation)){
                Files.createDirectories(thumbnailLocation);
            }
        } catch (Exception ex) {
            throw new ThumbnailUploadException("썸네일 생성 디렉토리를 추가하는 데 실패했습니다.");
        }

        try{
            if(!Files.exists(resizedLocation)){
                Files.createDirectory(resizedLocation);
            }
        } catch (Exception ex){
            ex.printStackTrace();
            // throw new VideoUploadException("리사이징 영상 생성 디렉토리를 추가하는 데 실패했습니다.");
        }

    }
    
    public String createThumbnail(String targetFileName){
        String targetFilePath = originPath + targetFileName;
        String resultFileName = FilenameUtils.getBaseName(targetFileName) + thumbnailSuffix + resultThumbnailExt;
        String resultFilePath = thumbnailSavepath + resultFileName;
        videoThumbnailer.createThumbnail(targetFilePath, resultFilePath);
        return resultFileName;
    }


    public String resizeWidth(String originalFileName, int resizeWidth){
        String targetFilePath = originPath + originalFileName;
        String resultFileName = FilenameUtils.getBaseName(originalFileName) + "_" + resizeWidth + "." + FilenameUtils.getExtension(originalFileName);
        String resultFilePath = convertSavePath + resultFileName;
        videoResizer.resizeWidth(targetFilePath,resultFilePath, resizeWidth);
        return resultFileName;
    }

    public String getVideoUrl(String fileName){
        return videoUrl + "/" + fileName;
    }

    public String getThumbnailUrl(String fileName){
        return thumbnailUrl + "/" + fileName;
    }

    public String getSavedPathFromUrl(String url){
        String fileName = FilenameUtils.getName(url);
        return convertSavePath + fileName;
    }

    public WebVideoMetaInfo getWebVideoMetaInfoByFileName(String fileName){
        VideoMetaInfo info = getMataInfoByFileName(fileName);
        String url = getVideoUrl(fileName);
        return WebVideoMetaInfo.builder()
                                .filesize(info.getFilesize())
                                .width(info.getWidth())
                                .height(info.getHeight())
                                .videoUrl(url)
                                .build();
    }

    public Integer getResizeProgress(String videoPath){
        return videoResizer.getProgress(videoPath);
    }
    
    abstract public VideoMetaInfo getMataInfoByFileName(String fileName);

    public Integer getThumbnailProgress(String thumbnailPath) {
        return videoThumbnailer.getProgress(thumbnailPath);
    }

}

