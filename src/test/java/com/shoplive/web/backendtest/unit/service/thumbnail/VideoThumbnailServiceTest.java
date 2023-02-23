package com.shoplive.web.backendtest.unit.service.thumbnail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.shoplive.web.backendtest.dao.VideoDao;
import com.shoplive.web.backendtest.entity.Video;
import com.shoplive.web.backendtest.exception.VideoUploadException;
import com.shoplive.web.backendtest.helper.VideoUploadHelper;
import com.shoplive.web.backendtest.mapper.VideoMapper;
import com.shoplive.web.backendtest.service.thumbnail.DefaultVideoThumbnailService;
import com.shoplive.web.backendtest.service.thumbnail.VideoThumbnailService;

public class VideoThumbnailServiceTest {

    @Mock
    private VideoDao videoDao;

    @Mock
    private VideoUploadHelper videoUploadHelper;

    @Mock
    private VideoMapper videoMapper;

    private VideoThumbnailService videoThumbnailService;

    
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        videoThumbnailService = new DefaultVideoThumbnailService(videoUploadHelper, videoDao);
    }

    @Test
    void createThumbnailTest(){
        String fileName = "test_sample.mp4";
        when(videoUploadHelper.createThumbnail(fileName)).thenReturn("test_sample_thumb.gif");
        String result = "";
        try {
            result = videoThumbnailService.createThumbnail(fileName);
        } catch (IOException e) {
            Assertions.fail();
        }
        assertEquals("test_sample_thumb.gif", result);
    }

    @Test
    void getThumbnailProgerssTest(){
        Long videoId1 = 1L;
        Long videoId2 = 2L;
        String originalVideoUrl = "test.mp4";
        String savedPath = "/usr/local/video/test.mp4";
        Video sampleVideo1 = Video.builder()
                                .id(videoId1)
                                .originalVideoUrl(originalVideoUrl)
                                .thumbnailUrl("http://localhost:8080/thumb")
                                .build();
        Video sampleVideo2 = Video.builder()
                                .id(videoId2)
                                .originalVideoUrl(originalVideoUrl)
                                .build();
        when(videoDao.getById(videoId1)).thenReturn(sampleVideo1);
        when(videoDao.getById(videoId2)).thenReturn(sampleVideo2);
        when(videoUploadHelper.getSavedPathFromUrl(originalVideoUrl)).thenReturn(savedPath);
        when(videoUploadHelper.getThumbnailProgress(savedPath)).thenReturn(30);
    
        assertEquals("100%", videoThumbnailService.getProgress(videoId1).getProgress());
        assertEquals("30%",videoThumbnailService.getProgress(videoId2).getProgress());
    
    }

    @Test
    void getThumbnailProgerssExceptionTest(){
        Long videoId1 = 1L;

        when(videoDao.getById(videoId1)).thenReturn(null);
       
        assertThrows(VideoUploadException.class, () -> videoThumbnailService.getProgress(videoId1));
    }
}
