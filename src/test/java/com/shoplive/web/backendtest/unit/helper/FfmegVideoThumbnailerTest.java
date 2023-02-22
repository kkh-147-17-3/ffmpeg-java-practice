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

import com.shoplive.web.backendtest.helper.FfmpegVideoThumbnailer;
import com.shoplive.web.backendtest.helper.ProgressHandler;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

public class FfmegVideoThumbnailerTest {

    @Mock
    ProgressHandler progressHandler;

    FfmpegVideoThumbnailer videoThumbnailer;

    public final String targetFilePath = Paths.get("./test_sample.mp4").toAbsolutePath().normalize().toString();

    public final String thumbnailExt = "gif";

    @BeforeEach
    public void setup() {
        try {
            FFmpeg ffmpeg = new FFmpeg("/usr/bin/ffmpeg");
            FFprobe ffprobe = new FFprobe("/usr/bin/ffprobe");
            MockitoAnnotations.openMocks(this);
            videoThumbnailer = new FfmpegVideoThumbnailer(ffmpeg, ffprobe);
        } catch (Exception e) {
            throw new TestAbortedException("ffmpeg, ffprobe 실행파일을 불러오는 데 실패했습니다. 설치 경로를 확인해주세요.");
        }
    }

    @Test
    public void createThumbnailTest() {

        String targetFileName = FilenameUtils.getName(targetFilePath);
        String resultFileName = FilenameUtils.getBaseName(targetFileName) + "_thumb." + thumbnailExt;
        String resultFilePath = "./" + resultFileName;
        videoThumbnailer.createThumbnail(targetFileName, resultFilePath);

        // TODO: 썸네일이 정확하게 생성되었는지 확인하는 방법은?
        FFmpegProbeResult thumbnailProbeResult = videoThumbnailer.getProbeResult(resultFilePath);
        FFmpegFormat thumbnailFormat = thumbnailProbeResult.getFormat();
        assertEquals(thumbnailExt, thumbnailFormat.format_name);

    }

    @Test
    public void upadateAndGetProgressTest() {
        videoThumbnailer.updateProgress(targetFilePath, 30);
        assertEquals(30, videoThumbnailer.getProgress(targetFilePath));
    }

    @Test
    public void updateAndDeleteProgressTest() {
        videoThumbnailer.updateProgress(targetFilePath, 30);
        videoThumbnailer.deleteProgress(targetFilePath);
        assertNull(videoThumbnailer.getProgress(targetFilePath));
    }

    @Test
    public void updateProgressExceptionTest() {
        assertThrows(RuntimeException.class, () -> {
            videoThumbnailer.updateProgress(targetFilePath, -30);
        });

        assertThrows(RuntimeException.class, () -> {
            videoThumbnailer.updateProgress(targetFilePath, 101);
        });
    }
}
