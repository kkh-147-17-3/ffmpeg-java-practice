package com.shoplive.web.backendtest.unit.service.upload;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.shoplive.web.backendtest.exception.ThumbnailUploadException;
import com.shoplive.web.backendtest.exception.VideoUploadException;
import com.shoplive.web.backendtest.helper.StorageProperties;
import com.shoplive.web.backendtest.request.VideoUploadRequest;
import com.shoplive.web.backendtest.service.VideoService;
import com.shoplive.web.backendtest.service.resize.VideoResizeService;
import com.shoplive.web.backendtest.service.thumbnail.VideoThumbnailService;
import com.shoplive.web.backendtest.service.upload.DefaultVideoUploadService;
import com.shoplive.web.backendtest.service.upload.VideoUploadService;

public class VideoUploadServiceTest {

    @Mock
    private VideoService videoService;

    @Mock
    private VideoThumbnailService videoThumbnailService;

    @Mock
    private VideoResizeService videoResizeService;

    private VideoUploadService videoUploadService;

    private final Long videoId = 1L;
    private final String fileName = "test_sample.mp4";
    private final String paramName = "videoFile";


    @BeforeEach
    public void setup() throws IOException{
        MockitoAnnotations.openMocks(this);
        StorageProperties properties = new StorageProperties();
        properties.setVideoUploadDir("/usr/local/test");
        videoUploadService = new DefaultVideoUploadService(videoService, videoThumbnailService, videoResizeService, properties);
    }

    @Test
    public void uploadTest(){
        byte bytes[] = new byte[1024 * 1024 * 100];
        Long resultVideoId = videoId;

        VideoUploadService spyService = Mockito.spy(videoUploadService);

        MockMultipartFile videoFile = new MockMultipartFile(paramName,fileName,MediaType.MULTIPART_FORM_DATA_VALUE,bytes);
        VideoUploadRequest request = VideoUploadRequest.builder().title("test").build();
        doReturn(resultVideoId).when(spyService).create(videoFile, request);
        doNothing().when(spyService).createThumbnail(resultVideoId, fileName);
        doNothing().when(spyService).createResized(resultVideoId, fileName);

        assertEquals(resultVideoId,spyService.upload(videoFile, request).getId());
    }

    @Test
    public void createResizedTest(){

        when(videoResizeService.createResized(fileName)).thenReturn(CompletableFuture.completedFuture(fileName));
        when(videoService.updateResizedInfo(videoId, fileName)).thenReturn(1);

        assertDoesNotThrow(()->videoUploadService.createResized(videoId, fileName));
    }

    public void createThumbnailTest(){
        String thumbnailFileName = "test_sample_thumb.gif";
        VideoUploadService spyService = Mockito.spy(videoUploadService);
        try {
            when(videoThumbnailService.createThumbnail(fileName)).thenReturn(thumbnailFileName);
            when(videoService.updateThumbnailUrl(videoId, thumbnailFileName)).thenReturn(1);
            doNothing().when(spyService).createResized(videoId, fileName);
            spyService.createThumbnail(videoId, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
    @Test
    public void createThumbnailExceptionTest(){
        String thumbnailFileName = "test_sample_thumb.gif";
        VideoUploadService spyService = Mockito.spy(videoUploadService);
        try {
            when(videoThumbnailService.createThumbnail(fileName)).thenReturn(thumbnailFileName);
            when(videoService.updateThumbnailUrl(videoId, thumbnailFileName)).thenReturn(0);
            doNothing().when(spyService).createResized(videoId, fileName);
        } catch (IOException e) {
            Assertions.fail();
        }
        assertThrows(ThumbnailUploadException.class, ()->spyService.createThumbnail(videoId, fileName));
    }
}
