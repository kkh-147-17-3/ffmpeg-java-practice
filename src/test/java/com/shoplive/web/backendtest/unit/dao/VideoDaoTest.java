package com.shoplive.web.backendtest.unit.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import com.shoplive.web.backendtest.dao.VideoDao;
import com.shoplive.web.backendtest.entity.Video;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VideoDaoTest {
    
    @Autowired
    private VideoDao videoDao;
    
    private Video sampleVideo1, sampleVideo2;

    @BeforeEach
    void prepareSampleVideo(){
        sampleVideo1 = Video.builder()
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
            .build();

        sampleVideo2 = Video.builder()
            .title("test sample2")
            .originalFilesize(2000000)
            .originalWidth(3600)
            .originalHeight(2000)
            .originalVideoUrl("http://localhost:8080/path/to/video/test_sample2.mp4")
            .resizedFilesize(1000000)
            .resizedWidth(720)
            .resizedHeight(400)
            .resizedVideoUrl("http://localhost:8080/path/to/video/test_sample2_360.mp4")
            .thumbnailUrl("http://localhost:8080/path/to/video/test_sample2_thumb.gif")
            .build();
    }

    @Test
    public void insertAndGetTest(){
        
        videoDao.insert(sampleVideo1);
        Video insertedVideo = videoDao.getById(sampleVideo1.getId());
        assertNotNull(insertedVideo);
        assertNotNull(insertedVideo.getId());
        assertEquals(sampleVideo1.getTitle(),insertedVideo.getTitle());
        assertEquals(sampleVideo1.getOriginalFilesize(),insertedVideo.getOriginalFilesize());
        assertEquals(sampleVideo1.getOriginalWidth(),insertedVideo.getOriginalWidth());
        assertEquals(sampleVideo1.getOriginalHeight(),insertedVideo.getOriginalHeight());
        assertEquals(sampleVideo1.getOriginalVideoUrl(),insertedVideo.getOriginalVideoUrl());
        assertEquals(sampleVideo1.getResizedFilesize(),insertedVideo.getResizedFilesize());
        assertEquals(sampleVideo1.getResizedWidth(),insertedVideo.getResizedWidth());
        assertEquals(sampleVideo1.getResizedHeight(),insertedVideo.getResizedHeight());
        assertEquals(sampleVideo1.getResizedVideoUrl(),insertedVideo.getResizedVideoUrl());
        assertNotNull(insertedVideo.getCreatedAt());
    }

    @Test
    public void insertAndUpdateTest(){
        videoDao.insert(sampleVideo1);
        Video insertedVideo = videoDao.getById(sampleVideo1.getId());
        sampleVideo2.setId(insertedVideo.getId());
        videoDao.update(sampleVideo2);
        Video updatedVideo = videoDao.getById(sampleVideo2.getId());
        assertEquals(insertedVideo.getId(), updatedVideo.getId());
        assertEquals(sampleVideo2.getTitle(),updatedVideo.getTitle());
        assertEquals(sampleVideo2.getOriginalFilesize(),updatedVideo.getOriginalFilesize());
        assertEquals(sampleVideo2.getOriginalWidth(), updatedVideo.getOriginalWidth());
        assertEquals(sampleVideo2.getOriginalHeight(), updatedVideo.getOriginalHeight());
        assertEquals(sampleVideo2.getOriginalVideoUrl(),updatedVideo.getOriginalVideoUrl());
        assertEquals(sampleVideo2.getResizedFilesize(),updatedVideo.getResizedFilesize());
        assertEquals(sampleVideo2.getResizedWidth(), updatedVideo.getResizedWidth());
        assertEquals(sampleVideo2.getOriginalVideoUrl(), updatedVideo.getOriginalVideoUrl());
        assertEquals(insertedVideo.getCreatedAt(), updatedVideo.getCreatedAt());
    }
}
