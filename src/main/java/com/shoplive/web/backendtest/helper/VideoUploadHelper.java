package com.shoplive.web.backendtest.helper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.shoplive.web.backendtest.exception.ThumbnailUploadException;
import com.shoplive.web.backendtest.exception.VideoUploadException;
import com.shoplive.web.backendtest.interfaces.ProgressibleVideoResizer;
import com.shoplive.web.backendtest.interfaces.ProgressibleVideoThumbnailer;
import com.shoplive.web.backendtest.model.VideoMetaInfo;
import com.shoplive.web.backendtest.model.WebVideoMetaInfo;

import jakarta.annotation.PostConstruct;
import lombok.Setter;

@Setter
@ConfigurationProperties(prefix="video")
public abstract class VideoUploadHelper {

    protected String originPath;
    private String videoUrl;
    private String originUrl;
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
    public void init(){
        Path resizedLocation = Paths.get(convertSavePath).toAbsolutePath().normalize();
        Path thumbnailLocation = Paths.get(thumbnailSavepath).toAbsolutePath().normalize();
        try {
            if(!Files.exists(thumbnailLocation)){
                Files.createDirectories(thumbnailLocation);
            }
        } catch (Exception ex) {
            throw new ThumbnailUploadException("썸네일 생성 디렉터리를 추가하는 데 실패했습니다.");
        }

        try{
            if(Files.exists(resizedLocation)) return;

            Files.createDirectory(resizedLocation);
        } catch (Exception ex){
            throw new VideoUploadException("리사이징 영상 생성 디렉토리를 추가하는 데 실패했습니다.");
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
        return originUrl + videoUrl + "/" + fileName;
    }

    public String getThumbnailUrl(String fileName){
        return originUrl + thumbnailUrl + "/" + fileName;
    }

    public String getSavedPathFromUrl(String url){
        String fileName = FilenameUtils.getName(url);
        return convertSavePath + fileName;
    }

    public WebVideoMetaInfo getWebVideoMetaInfoByFileName(String fileName){
        VideoMetaInfo info = getMetaInfoByFileName(fileName);
        String url = getVideoUrl(fileName);
        return WebVideoMetaInfo.builder()
                                .filesize(info.getFilesize())
                                .width(info.getWidth())
                                .height(info.getHeight())
                                .videoUrl(url)
                                .build();
    }
    
    abstract public VideoMetaInfo getMetaInfoByFileName(String fileName);

    public Integer getResizeProgress(String videoPath){
        return videoResizer.getProgress(videoPath);
    }
    
    public Integer getThumbnailProgress(String thumbnailPath) {
        return videoThumbnailer.getProgress(thumbnailPath);
    }

}

