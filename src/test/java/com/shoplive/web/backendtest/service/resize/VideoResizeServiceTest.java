package com.shoplive.web.backendtest.service.resize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.shoplive.web.backendtest.dao.VideoDao;
import com.shoplive.web.backendtest.entity.Video;
import com.shoplive.web.backendtest.exception.VideoUploadException;
import com.shoplive.web.backendtest.helper.VideoUploadHelper;
import com.shoplive.web.backendtest.mapper.VideoMapper;

public class VideoResizeServiceTest {

    @Mock
    private VideoDao videoDao;

    @Mock
    private VideoUploadHelper videoUploadHelper;

    @Mock
    private VideoMapper videoMapper;

    private VideoResizeService videoResizeService;

    
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        videoResizeService = new DefaultVideoResizeService(videoUploadHelper, videoDao);
    }

    @Test
    void createResizedTest(){
        String fileName = "test_sample.mp4";
        when(videoUploadHelper.resizeWidth(anyString(), anyInt())).thenReturn("test_sample_360.mp4");
        String result = videoResizeService.createResized(fileName);
        assertEquals(result, "test_sample_360.mp4");
    }

    @Test
    void getProgressTest(){
        String videoUrl = "http://localhost:8080/path/to/video/test_sample.mp4";
        when(videoDao.getById(1L))
            .thenReturn(Video.builder()
                            .resizedFilesize(100L)
                            .build());

        when(videoDao.getById(2L))
        .thenReturn(Video.builder()
                        .resizedFilesize(0)
                        .originalVideoUrl(videoUrl)
                        .build());
        when(videoDao.getById(3L))
        .thenReturn(null);

        when(videoUploadHelper.getResizedPathFromUrl(videoUrl)).thenReturn("/usr/local/shoplive/video/test_sample.mp4");
        when(videoUploadHelper.getResizeProgress("/usr/local/shoplive/video/test_sample.mp4")).thenReturn(30);
        
        assertEquals("100%", videoResizeService.getProgress(1L).getProgress());

        assertEquals("30%", videoResizeService.getProgress(2L).getProgress());

        assertThrows(VideoUploadException.class, ()->{
            videoResizeService.getProgress(3L);
        });

    }
}
