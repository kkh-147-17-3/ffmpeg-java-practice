package com.shoplive.web.backendtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.shoplive.web.backendtest.dao.VideoDao;
import com.shoplive.web.backendtest.dto.VideoDetailsResponse;
import com.shoplive.web.backendtest.dto.VideoMetaInfo;
import com.shoplive.web.backendtest.dto.VideoUploadRequest;
import com.shoplive.web.backendtest.entity.Video;
import com.shoplive.web.backendtest.service.DefaultVideoService;
import com.shoplive.web.backendtest.service.VideoService;
import com.shoplive.web.backendtest.util.VideoUploadUtil;

public class VideoServiceTest {

    @Mock
    private VideoDao videoDao;

    @Mock
    private VideoUploadUtil videoUploadUtil;

    private VideoService videoService;

    private Video sampleVideo;
    
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        videoService = new DefaultVideoService(videoDao, videoUploadUtil);
    }

    @BeforeEach
    public void prepareSampleVideo(){
        sampleVideo = Video.builder()
            .id(1L)
            .title("test sample1")
            .originalFilesize(1000000)
            .originalWidth(1800)
            .originalHeight(1000)
            .originalVideoUrl("http://localhost:8080/path/to/video/test_sample1.mp4")
            .resizedFilesize(500000)
            .resizedWidth(360)
            .resizedHeight(200)
            .resizedVideoUrl("http://localhost:8080/path/to/video/test_sample1_360.mp4")
            .thumbnailUrl("http://localhost:8080/path/to/video/test_sample1_thumb.gif")
            .createdAt(LocalDateTime.of(2023, 2, 16, 6, 0, 0))
            .build();
    }

    @Test
    void getDetailsTest() throws IOException{

        Long videoId = sampleVideo.getId();
        when(videoDao.getById(sampleVideo.getId())).thenReturn(sampleVideo);

        VideoDetailsResponse response = videoService.getDetails(videoId);
        
        assertEquals(sampleVideo.getId(), response.getId());
        assertEquals(sampleVideo.getTitle(), response.getTitle());
        assertEquals(sampleVideo.getOriginalFilesize(),response.getOriginal().getFilesize());
        assertEquals(sampleVideo.getOriginalWidth(),response.getOriginal().getWidth());
        assertEquals(sampleVideo.getOriginalHeight(),response.getOriginal().getHeight());
        assertEquals(sampleVideo.getOriginalVideoUrl(),response.getOriginal().getVideoUrl());
        assertEquals(sampleVideo.getResizedFilesize(),response.getResized().getFilesize());
        assertEquals(sampleVideo.getResizedWidth(),response.getResized().getWidth());
        assertEquals(sampleVideo.getResizedHeight(),response.getResized().getHeight());
        assertEquals(sampleVideo.getResizedVideoUrl(),response.getResized().getVideoUrl());
        assertEquals("2023-02-16T06:00:00+09:00", response.getCreatedAt());

    }

    @Test
    void insertTest(){
        String insertedFileName = "test_sample1.mp4";
        VideoUploadRequest requestDto = new VideoUploadRequest(sampleVideo.getTitle());
        VideoMetaInfo insertedVideoMetaInfo = VideoMetaInfo.builder()
                                                            .filesize(sampleVideo.getOriginalFilesize())
                                                            .width(sampleVideo.getOriginalWidth())
                                                            .height(sampleVideo.getOriginalHeight())
                                                            .videoUrl(sampleVideo.getOriginalVideoUrl())
                                                            .build();

        when(videoUploadUtil.getMetaInfoByFileName(insertedFileName))
                            .thenReturn(insertedVideoMetaInfo);
        
        when(videoDao.insert(any(Video.class))).thenAnswer(invocation -> {
            Video video = (Video)invocation.getArguments()[0];
            video.setId(1L);
            return 1;
        });
        
        Video insertedVideo = videoService.insert(insertedFileName, requestDto);

        verify(videoDao, times(1)).insert(any(Video.class));
        assertNotNull(insertedVideo.getId());
        assertEquals(insertedVideoMetaInfo.getFilesize(),insertedVideo.getOriginalFilesize());
        assertEquals(insertedVideoMetaInfo.getWidth(), insertedVideo.getOriginalWidth());
        assertEquals(insertedVideoMetaInfo.getHeight(), insertedVideo.getOriginalHeight());
        assertEquals(insertedVideoMetaInfo.getVideoUrl(), insertedVideo.getOriginalVideoUrl());
    }

    @Test
    void updateResizedInfoTest(){
        String resizedFileName = "test_sample1_360.mp4";
        Long videoId = sampleVideo.getId();
        VideoMetaInfo resizedVideoMetaInfo = VideoMetaInfo.builder()
                                                            .filesize(sampleVideo.getResizedFilesize())
                                                            .width(sampleVideo.getResizedWidth())
                                                            .height(sampleVideo.getResizedHeight())
                                                            .videoUrl(sampleVideo.getResizedVideoUrl())
                                                            .build();
        when(videoUploadUtil.getMetaInfoByFileName(resizedFileName))
                            .thenReturn(resizedVideoMetaInfo);
        
        when(videoDao.update(any(Video.class))).thenReturn(1);

        int result = videoService.updateResizedInfo(videoId, resizedFileName);

        verify(videoDao, times(1)).update(any(Video.class));
        assertEquals(result, 1);
    }

    @Test
    void updateThumbnailUrlTest(){
        String thumbnailFileName = "test_sample1_thumb.mp4";
        Long videoId = sampleVideo.getId();
        
        when(videoDao.update(any(Video.class))).thenReturn(1);

        int result = videoService.updateThumbnailUrl(videoId, thumbnailFileName);

        verify(videoDao, times(1)).update(any(Video.class));
        assertEquals(result, 1);
    }
}
