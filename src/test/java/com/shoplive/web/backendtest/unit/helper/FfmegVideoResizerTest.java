package com.shoplive.web.backendtest.unit.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opentest4j.TestAbortedException;

import com.shoplive.web.backendtest.helper.FfmpegVideoResizer;
import com.shoplive.web.backendtest.helper.ProgressHandler;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

public class FfmegVideoResizerTest {

    @Mock
    ProgressHandler progressHandler;

    FfmpegVideoResizer videoResizer;

    public final String targetFilePath = Paths.get("./test_sample.mp4").toAbsolutePath().normalize().toString();

    @BeforeEach
    public void setup(){
        try {
            FFmpeg ffmpeg = new FFmpeg("/usr/bin/ffmpeg");
            FFprobe ffprobe = new FFprobe("/usr/bin/ffprobe");
            MockitoAnnotations.openMocks(this);
            videoResizer = new FfmpegVideoResizer(ffmpeg, ffprobe);
        } catch (Exception e) {
            throw new TestAbortedException("ffmpeg, ffprobe 실행파일을 불러오는 데 실패했습니다. 설치 경로를 확인해주세요.");
        }
    }

    @Test
    public void resizeWidthTest(){

        int[] resizeWidths = {100,360,1500};
        String resultFilePath = "";
        for (int resizeWidth : resizeWidths){
            String targetFileName = FilenameUtils.getName(targetFilePath);
            String resultFileName = FilenameUtils.getBaseName(targetFileName) + "_" + resizeWidth 
                                    + "." + FilenameUtils.getExtension(targetFileName);
            resultFilePath = "./" + resultFileName;
            videoResizer.resizeWidth(targetFilePath, resultFilePath, resizeWidth);

            // TODO: 변환된 영상의 품질이 원본과 동일한 지 어떻게 검증하는지?
            // ex: bit rate, psnr 등...
            FFmpegProbeResult resizedProbeResult = videoResizer.getProbeResult(resultFilePath);
            FFmpegStream resizedStream = resizedProbeResult.getStreams().get(0);
            assertEquals(resizeWidth, resizedStream.width);
        }

    }

    @Test
    public void upadateAndGetProgressTest(){
        videoResizer.updateProgress(targetFilePath, 30);
        assertEquals(30, videoResizer.getProgress(targetFilePath));
    }

    @Test
    public void updateAndDeleteProgressTest(){
        videoResizer.updateProgress(targetFilePath, 30);
        videoResizer.deleteProgress(targetFilePath);
        assertNull(videoResizer.getProgress(targetFilePath));
    }
    
    @Test
    public void updateProgressExceptionTest(){
        assertThrows(RuntimeException.class, ()->{
            videoResizer.updateProgress(targetFilePath, -30);
        });

        assertThrows(RuntimeException.class, ()->{
            videoResizer.updateProgress(targetFilePath, 101);
        });
    }
}
