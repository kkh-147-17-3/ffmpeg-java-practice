package com.shoplive.web.backendtest.service.thumbnail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.shoplive.web.backendtest.dao.VideoDao;
import com.shoplive.web.backendtest.helper.VideoUploadHelper;
import com.shoplive.web.backendtest.mapper.VideoMapper;

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
}
