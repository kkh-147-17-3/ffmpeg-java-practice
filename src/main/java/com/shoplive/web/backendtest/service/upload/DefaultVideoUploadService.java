package com.shoplive.web.backendtest.service.upload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shoplive.web.backendtest.exception.ThumbnailUploadException;
import com.shoplive.web.backendtest.exception.VideoUploadException;
import com.shoplive.web.backendtest.helper.StorageProperties;
import com.shoplive.web.backendtest.request.VideoUploadRequest;
import com.shoplive.web.backendtest.response.VideoUploadResponse;
import com.shoplive.web.backendtest.service.VideoService;
import com.shoplive.web.backendtest.service.resize.VideoResizeService;
import com.shoplive.web.backendtest.service.thumbnail.VideoThumbnailService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultVideoUploadService implements VideoUploadService {

    private final VideoService videoService;
    private final VideoThumbnailService thumbnailService;
    private final VideoResizeService resizeService;
    private final StorageProperties properties;
    private Path rootLocation;

    @Override
    @PostConstruct
    public void init() {
        rootLocation = Paths.get(properties.getVideoUploadDir()).toAbsolutePath().normalize();
        try {
            if(Files.exists(rootLocation)) return;

            Files.createDirectory(rootLocation);
        } catch (IOException e) {

            e.printStackTrace();
            throw new VideoUploadException ("업로드 저장 경로를 초기화할 수 없습니다.");

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoUploadResponse upload(MultipartFile videoFile, VideoUploadRequest request) {
        String fileName = StringUtils.cleanPath(videoFile.getOriginalFilename());
        Long videoId = create(videoFile, request);
        createThumbnail(videoId, fileName);
        createResized(videoId, fileName);
        return VideoUploadResponse.builder().id(videoId).build();             
    }

    // 생성된 파일의 videoId 반환
    @Override
    public Long create(MultipartFile videoFile, VideoUploadRequest request) {
        
        // Normalize file name
        String fileName = StringUtils.cleanPath(videoFile.getOriginalFilename());
        try
        {
            if (videoFile.isEmpty()) {
                throw new VideoUploadException ("비어있는 파일을 업로드할 수 없습니다. " + videoFile.getOriginalFilename());
            }

            String ext = FilenameUtils.getExtension(fileName);

            // mp4 확장자가 아닌 경우 예외처리
            if (!ext.equals("mp4")){
                throw new VideoUploadException ("지원하지 않는 확장자입니다. " + ext);
            }            
            Path targetLocation = this.rootLocation.resolve(fileName);
            Files.copy(videoFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            // throw new VideoUploadException ("파일을 저장하는데 실패했습니다. " + videoFile.getOriginalFilename() + " 사유 : " + e.getMessage().toString());
        }
        Long videoId = videoService.insert(fileName, request).getId();
        return videoId;
    }

    @Override
    public void createResized(Long videoId, String fileName){
            resizeService.createResized(fileName)
                    .thenAccept(resizedFileName-> videoService.updateResizedInfo(videoId, resizedFileName));
    }

    @Override
    public void createThumbnail(Long videoId, String fileName){
        createResized(videoId, fileName);
        try {
            String thumbnailFileName = thumbnailService.createThumbnail(fileName);
            int result = videoService.updateThumbnailUrl(videoId, thumbnailFileName);
            if(result != 1) throw new ThumbnailUploadException("썸네일 정보 업데이트에 실패했습니다");
        } catch (IOException e){
            throw new ThumbnailUploadException("썸네일 생성에 실패했습니다.");
        }
    }
}
