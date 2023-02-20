package com.shoplive.web.backendtest.service;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shoplive.web.backendtest.dao.VideoDao;
import com.shoplive.web.backendtest.dto.VideoDetailsResponse;
import com.shoplive.web.backendtest.dto.VideoMetaInfo;
import com.shoplive.web.backendtest.dto.VideoUploadRequest;
import com.shoplive.web.backendtest.entity.Video;
import com.shoplive.web.backendtest.exception.VideoUploadException;
import com.shoplive.web.backendtest.mapper.VideoMapper;
import com.shoplive.web.backendtest.util.VideoUploadUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultVideoService implements VideoService{

    private final VideoDao dao;
    private final VideoUploadUtil util;
    private final VideoMapper videoMapper = Mappers.getMapper(VideoMapper.class);
    

    @Value("${resource.origin}")
    private String origin;

    @Value("${resource.thumbnail-url}")
    private String thumbnailUrl;

    @Override
    public Video insert(String originalFileName, VideoUploadRequest dto) {
        
        VideoMetaInfo info = util.getMetaInfoByFileName(originalFileName);

        Video video = Video.builder()
                            .title(dto.getTitle())
                            .originalFilesize(info.getFilesize())
                            .originalWidth(info.getWidth())
                            .originalHeight(info.getHeight())
                            .originalVideoUrl(info.getVideoUrl())
                            .build();

        dao.insert(video);
        return video;
    }
    
    
    @Override
    public int updateResizedInfo(Long videoId, String resizedFileName) throws VideoUploadException{

        VideoMetaInfo info = util.getMetaInfoByFileName(resizedFileName);
        
        Video video = Video.builder()
                            .id(videoId)
                            .resizedFilesize(info.getFilesize())
                            .resizedWidth(info.getWidth())
                            .resizedHeight(info.getHeight())
                            .resizedVideoUrl(info.getVideoUrl())
                            .build();
        
        int result = dao.update(video);

        if (result != 1) throw new VideoUploadException ("리사이징 정보를 DB에 업데이트하는 데 실패했습니다.");

        return result;
    }

    @Override
    public int updateThumbnailUrl(Long videoId, String thumbnailFileName) throws VideoUploadException{

        Video video = Video.builder()
                            .id(videoId)
                            .thumbnailUrl(origin + thumbnailUrl+"/"+thumbnailFileName)
                            .build();
                            
        int result = dao.update(video);
        if (result != 1) throw new VideoUploadException("썸네일 정보를 DB에 업데이트하는 데 실패했습니다.");
        
        return result;
    }


    @Override
    public VideoDetailsResponse getDetails(Long id) {
        
        Video video = dao.getById(id);
        System.out.println(video);
        return videoMapper.getDetailsResponseFromEntity(video);
    }
}
