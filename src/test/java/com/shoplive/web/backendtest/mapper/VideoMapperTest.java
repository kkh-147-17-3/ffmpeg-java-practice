package com.shoplive.web.backendtest.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.shoplive.web.backendtest.entity.Video;
import com.shoplive.web.backendtest.response.VideoDetailsResponse;

public class VideoMapperTest {
    
    private final VideoMapper mapper = new VideoMapperImpl();


    @Test
    public void videoToVideoDetailsResponseTest(){
        Long testId = 1L;
        Video video = Video.builder()
                            .id(testId)
                            .title("test sample")
                            .originalFilesize(1000000)
                            .originalWidth(1800)
                            .originalHeight(1000)
                            .originalVideoUrl("http://localhost:8080/path/to/video/test_sample.mp4")
                            .resizedFilesize(500000)
                            .resizedWidth(360)
                            .resizedHeight(200)
                            .resizedVideoUrl("http://localhost:8080/path/to/video/test_sample_360.mp4")
                            .thumbnailUrl("http://localhost:8080/path/to/video/test_sample_thumb.gif")
                            .createdAt(LocalDateTime.of(2023, 2, 16, 6, 0, 0))
                            .build();
        
        VideoDetailsResponse response = mapper.getDetailsResponseFromEntity(video);

        assertEquals(video.getId(),response.getId());
        assertEquals(video.getTitle(),response.getTitle(),response.getTitle());
        assertEquals(video.getOriginalFilesize(),response.getOriginal().getFilesize());
        assertEquals(video.getOriginalWidth(),response.getOriginal().getWidth());
        assertEquals(video.getOriginalHeight(),response.getOriginal().getHeight());
        assertEquals(video.getOriginalVideoUrl(),response.getOriginal().getVideoUrl());
        assertEquals(video.getResizedFilesize(),response.getResized().getFilesize());
        assertEquals(video.getResizedWidth(),response.getResized().getWidth());
        assertEquals(video.getResizedHeight(),response.getResized().getHeight());
        assertEquals(video.getResizedVideoUrl(),response.getResized().getVideoUrl());
        assertEquals(video.getThumbnailUrl(),response.getThumbnailUrl());
    }
}
