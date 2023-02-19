package com.shoplive.web.backendtest.service;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shoplive.web.backendtest.dao.VideoDao;
import com.shoplive.web.backendtest.dto.VideoDetailsResponse;
import com.shoplive.web.backendtest.dto.VideoUploadRequest;
import com.shoplive.web.backendtest.entity.Video;
import com.shoplive.web.backendtest.mapper.VideoMapper;
import com.shoplive.web.backendtest.util.VideoUploadUtil;

import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

@Service
@RequiredArgsConstructor
public class DefaultVideoService implements VideoService{

    private final VideoDao dao;
    private final VideoUploadUtil util;
    private final VideoMapper videoMapper = Mappers.getMapper(VideoMapper.class);
    
    @Value("${resource.origin}")
    private String origin;

    @Value("${resource.video-url}")
    private String videoUrl;
    @Value("${resource.thumbnail-url}")
    private String thumbnailUrl;

    @Override
    public Video insert(String originalFileName, VideoUploadRequest dto) {
        
        FFmpegProbeResult originalProbeResult = util.getProbeResult(originalFileName);
        FFmpegStream originalStream = originalProbeResult.getStreams().get(0);
        FFmpegFormat originalFormat = originalProbeResult.getFormat();


        Video video = Video.builder()
                            .title(dto.getTitle())
                            .originalFilesize(originalFormat.size)
                            .originalWidth(originalStream.width)
                            .originalHeight(originalStream.height)
                            .originalVideoUrl(origin + videoUrl+"/"+originalFileName)
                            .build();


        dao.insert(video);
        return video;
    }
    
    
    @Override
    public int updateResizedInfo(Long videoId, String resizedFileName) {

        FFmpegProbeResult resizedProbeResult = util.getProbeResult(resizedFileName);
        FFmpegStream resizedStream = resizedProbeResult.getStreams().get(0);
        FFmpegFormat resizedFormat = resizedProbeResult.getFormat();
        
        Video video = Video.builder()
                            .id(videoId)
                            .resizedFilesize(resizedFormat.size)
                            .resizedWidth(resizedStream.width)
                            .resizedHeight(resizedStream.height)
                            .resizedVideoUrl(origin +videoUrl+"/"+resizedFileName)
                            .build();
        
        System.out.println(video);

        return dao.updateById(video);
    }

    @Override
    public int updateThumbnailUrl(Long videoId, String thumbnailFileName) {

        Video video = Video.builder()
                            .id(videoId)
                            .thumbnailUrl(origin + thumbnailUrl+"/"+thumbnailFileName)
                            .build();
        System.out.println(video);
        return dao.updateById(video);
    }


    @Override
    public VideoDetailsResponse getDetails(Long id) {
        
        Video video = dao.getById(id);
        System.out.println(video);
        return videoMapper.getDetailsResponseFromEntity(video);
    }
}
