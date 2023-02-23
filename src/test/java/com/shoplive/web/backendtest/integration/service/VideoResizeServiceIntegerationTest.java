package com.shoplive.web.backendtest.integration.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shoplive.web.backendtest.exception.VideoUploadException;
import com.shoplive.web.backendtest.helper.VideoUploadHelper;
import com.shoplive.web.backendtest.request.VideoUploadRequest;
import com.shoplive.web.backendtest.service.resize.VideoResizeService;
import com.shoplive.web.backendtest.service.upload.VideoUploadService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VideoResizeServiceIntegerationTest {
    
    private final VideoResizeService videoResizeService;
    private final VideoUploadService videoUploadService;
    private final VideoUploadHelper videoUploadHelper;
    private final String originalFileName = "test_sample.mp4";
    private final String paramName = "videoFile";
    private VideoUploadRequest request;
    private MultipartFile videoFile;
    private Long videoId;

    @PostConstruct
    void init(){
        String classPath = Paths.get("./").toAbsolutePath().normalize().toString();
        File file = new File(classPath + "/" + originalFileName);
        try {
            InputStream targetStream = new FileInputStream(file);
            videoFile = new MockMultipartFile(paramName,originalFileName,MediaType.MULTIPART_FORM_DATA_VALUE,targetStream);
            request = VideoUploadRequest.builder().title("test").build();
            videoId = videoUploadService.create(videoFile, request);
            videoUploadHelper.setOriginPath(classPath + "/");
            videoUploadHelper.setConvertSavePath(classPath + "/");
            videoUploadHelper.setThumbnailSavepath(classPath + "/");
        } catch (Exception e) {
            throw new TestAbortedException("초기화에 실패했습니다 : " + this.getClass() + "사유: "+ e.getMessage().toString());
        }
    }

    @Test
    public void createResizedTest(){
        try{
            String result = videoResizeService.createResized(originalFileName).get();
            assertEquals("test_sample_360.mp4", result);
        } catch (Exception e){
            Assertions.fail();
        }
    }

    @Test
    public void getProgressTest(){
        try {
            assertThrows(VideoUploadException.class,()->videoResizeService.getProgress(203L));
            videoResizeService.createResized(originalFileName);
            Thread.sleep(2);
            assertNotEquals("0%",videoResizeService.getProgress(videoId).getProgress());
        } catch (Exception e) {
            Assertions.fail();
        }
    }
}
