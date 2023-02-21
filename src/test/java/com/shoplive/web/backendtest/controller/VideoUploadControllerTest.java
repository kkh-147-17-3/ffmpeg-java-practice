package com.shoplive.web.backendtest.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.shoplive.web.backendtest.Request.VideoUploadRequest;
import com.shoplive.web.backendtest.Response.VideoDetailsResponse;
import com.shoplive.web.backendtest.Response.VideoProgressResponse;
import com.shoplive.web.backendtest.entity.Video;
import com.shoplive.web.backendtest.service.VideoService;
import com.shoplive.web.backendtest.service.resize.VideoResizeService;
import com.shoplive.web.backendtest.service.thumbnail.VideoThumbnailService;
import com.shoplive.web.backendtest.service.upload.VideoUploadService;

public class VideoUploadControllerTest {

    @Mock
    private VideoService videoService;

    @Mock
    private VideoUploadService videoUploadService;

    @Mock
    private VideoResizeService videoResizeService;

    @Mock
    private VideoThumbnailService videoThumbnailService;

    @InjectMocks
    private VideoUploadController videoUploadController;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mvc =  MockMvcBuilders.standaloneSetup(videoUploadController).build();
    }

    @Test
    void getViewTest(){
        try{
            mvc.perform(get("/video"))
                .andExpect(status().isOk())
                .andExpect(view().name("test"));
        }
        catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    void uploadRequestTest() throws IOException, Exception{
        String fileName = "test_sample.mp4";
        byte bytes[] = new byte[1024 * 1024 * 100];
        String paramName = "videoFile";
        MockMultipartFile videoFile = new MockMultipartFile(paramName,fileName,MediaType.MULTIPART_FORM_DATA_VALUE,bytes);
        MockMultipartFile requestJson = new MockMultipartFile("metaInfo", null, "application/json", "{\"title\": \"hello\"}".getBytes());
        
        when(videoUploadService.create(any(MultipartFile.class))).
            thenReturn("test_sample.mp4");
        when(videoService.insert(anyString(),any(VideoUploadRequest.class))).
            thenReturn(Video.builder().
                            id(1L).
                            build());

        when(videoResizeService.createResized("test_sample.mp4")).thenReturn("test_sample_360.mp4");
        when(videoThumbnailService.createThumbnail("test_sample.mp4")).thenReturn("test_sample_thumb.mp4");
        when(videoService.updateResizedInfo(anyLong(), anyString())).thenReturn(1);
        when(videoService.updateThumbnailUrl(anyLong(), anyString())).thenReturn(1);
        
        mvc.perform(
                        multipart("/video")
                        .file(videoFile).file(requestJson)
                    )
            .andExpect(status().isOk())
            .andExpect(content().json("{'id':1}"));
    }

    @Test
    void getDetailsTest(){
        Long videoId = 1L;
        VideoDetailsResponse response = VideoDetailsResponse.builder()
                                                            .id(videoId)
                                                            .build();

        when(videoService.getDetails(videoId)).thenReturn(response);

        try{
            mvc.perform(get("/video/" + String.valueOf(videoId)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(equalTo(videoId), Long.class));
        }
        catch (Exception e){
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void getProgressTest(){
        Long videoId = 1L;
        VideoProgressResponse response = VideoProgressResponse.builder()
                                    .id(videoId)
                                    .progress("100%")
                                    .build();
        when(videoResizeService.getProgress(videoId)).thenReturn(response);
        try{
            mvc.perform(get("/video/" + String.valueOf(videoId) + "/progress").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",equalTo(1)))
                .andExpect(jsonPath("$.progress",equalTo("100%")));
        }
        catch (Exception e){
            e.printStackTrace();
            Assertions.fail();
        }
    }


    // @Test
    // void blockOversizedFileUploadTest() throws Exception{
    //     byte[] bytes = new byte[1024 * 1024 * 10001];
    //     String paramName = "videoFile";

    //     MockMultipartFile videoFile = new MockMultipartFile(paramName,"test_sample.mp4",MediaType.MULTIPART_FORM_DATA_VALUE,bytes);
    //     MockMultipartFile requestJson = new MockMultipartFile("metaInfo", null, "application/json", "{\"title\": \"hello\"}".getBytes());

    //     bytes = new byte[1024 * 1024 * 101];
    //     mvc.perform(
    //         multipart("/video")
    //         .file(videoFile).file(requestJson)
    //     ).andExpect(status().isForbidden());
    // }

    
    
}
