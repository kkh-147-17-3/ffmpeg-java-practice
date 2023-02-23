package com.shoplive.web.backendtest.helper;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.shoplive.web.backendtest.model.VideoMetaInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

@RequiredArgsConstructor
@Getter
@Setter
@Component
public class FfmpegVideoHandler {
    
    private final FFmpeg ffMpeg;
    private final FFprobe ffProbe;
    
    public FFmpegProbeResult getProbeResult(String filePath) {
        FFmpegProbeResult ffmpegProbeResult;
        try {
            ffmpegProbeResult = ffProbe.probe(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ffmpegProbeResult;
    }

    public VideoMetaInfo getMetaInfoFromProbeResult(String filePath){
        FFmpegProbeResult probeResult = getProbeResult(filePath);
        FFmpegStream stream = probeResult.getStreams().get(0);
        FFmpegFormat format = probeResult.getFormat();

        VideoMetaInfo info = VideoMetaInfo.builder()
                                        .filesize(format.size)
                                        .width(stream.width)
                                        .height(stream.height)
                                        .build();
        return info;
    }
}
