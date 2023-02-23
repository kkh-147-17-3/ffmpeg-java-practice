package com.shoplive.web.backendtest.unit.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.shoplive.web.backendtest.helper.FfmpegVideoHandler;
import com.shoplive.web.backendtest.helper.FfmpegVideoUploadHelper;
import com.shoplive.web.backendtest.helper.VideoUploadHelper;
import com.shoplive.web.backendtest.interfaces.ProgressibleVideoResizer;
import com.shoplive.web.backendtest.interfaces.ProgressibleVideoThumbnailer;
import com.shoplive.web.backendtest.model.VideoMetaInfo;
import com.shoplive.web.backendtest.model.WebVideoMetaInfo;

public class FfmpegVideoUploadHelperTest {
    
    @Mock
    private ProgressibleVideoResizer videoResizer;

    @Mock
    private ProgressibleVideoThumbnailer videoThumbnailer;

    @Mock
    private FfmpegVideoHandler ffmpegVideoHandler;

    private FfmpegVideoUploadHelper videoUploadHelper;
    
    private final String targetFileName = "test_sample.mp4";


    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        videoUploadHelper = new FfmpegVideoUploadHelper(ffmpegVideoHandler);
        videoUploadHelper.setVideoResizer(videoResizer);
        videoUploadHelper.setVideoThumbnailer(videoThumbnailer);
        videoUploadHelper.setOriginPath("/usr/local/");
        videoUploadHelper.setVideoUrl("/video");
        videoUploadHelper.setThumbnailSavepath("/thumbnail/");
        videoUploadHelper.setThumbnailUrl("/thumb");
        videoUploadHelper.setConvertSavePath("/video/convert/");
        videoUploadHelper.setThumbnailSuffix("_thumb");
        videoUploadHelper.setOriginUrl("http://localhost:8080");
        System.out.println(videoThumbnailer);
    }

    @Test
    public void createThumbnailTest(){
        String resultFileName = "test_sample_thumb.gif";
        doNothing().when(videoThumbnailer).createThumbnail(anyString(), anyString());
        assertEquals(resultFileName, videoUploadHelper.createThumbnail(targetFileName));
    }

    @Test
    public void createResizeWidthTest(){
        int resizeWidth = 360;
        String resultFileName = "test_sample_360.mp4";
        doNothing().when(videoResizer).resizeWidth(anyString(), anyString(),anyInt());
        assertEquals(resultFileName, videoUploadHelper.resizeWidth(targetFileName,resizeWidth));
    }

    @Test 
    public void getVideoUrlTest(){
        String result = videoUploadHelper.getVideoUrl(targetFileName);
        assertEquals("http://localhost:8080/video/" + targetFileName, result);
    }

    @Test 
    public void getThumbnailUrlTest(){
        String result = videoUploadHelper.getThumbnailUrl(targetFileName);
        assertEquals("http://localhost:8080/thumb/" + targetFileName, result);
    }

    @Test
    public void getSavedPathFromUrlTest(){
        String testSample = "http://localhost:8080/video/" + targetFileName;
        String result = videoUploadHelper.getSavedPathFromUrl(testSample);
        assertEquals("/video/convert/" + targetFileName,result);
    }

    @Test
    public void  getWebVideoMetaInfoByFileNameTest(){
        VideoUploadHelper spyHelper = Mockito.spy(videoUploadHelper);
        VideoMetaInfo info = VideoMetaInfo.builder()
                                        .filesize(100)
                                        .width(200)
                                        .height(300)
                                        .build();

        String resultUrl = "http://localhost:8080/video/test_sample.mp4";
        doReturn(info).when(spyHelper).getMetaInfoByFileName(targetFileName);
        
        WebVideoMetaInfo resultInfo = spyHelper.getWebVideoMetaInfoByFileName(targetFileName);
        assertEquals(resultUrl,resultInfo.getVideoUrl());
        assertEquals(100,resultInfo.getFilesize());
        assertEquals(200, resultInfo.getWidth());
        assertEquals(300, resultInfo.getHeight());
    }

    
    @Test
    public void getMetaInfoByFileNameTest(){
        when(ffmpegVideoHandler.getMetaInfoFromProbeResult(anyString())).thenReturn(null);
        videoUploadHelper.getMetaInfoByFileName(targetFileName);
        verify(ffmpegVideoHandler, times(1)).getMetaInfoFromProbeResult(anyString());
    }
    
    @Test
    public void getResizeProgressTest(){
        when(videoResizer.getProgress(targetFileName)).thenReturn(100);
        assertEquals(100, videoUploadHelper.getResizeProgress(targetFileName));
    }

    @Test
    public void getThumbnailProgressTest(){
        when(videoThumbnailer.getProgress(targetFileName)).thenReturn(100);
        assertEquals(100, videoUploadHelper.getThumbnailProgress(targetFileName));
    }
}
