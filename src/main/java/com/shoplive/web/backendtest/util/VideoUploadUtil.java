package com.shoplive.web.backendtest.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;

@RequiredArgsConstructor
@ConfigurationProperties(prefix = "ffmpeg")
@Getter
@Setter
@Component
public class VideoUploadUtil {

    private final FFmpeg ffMpeg;
    private final FFprobe ffProbe;

    @Value("${file.video-upload-dir}")
    private String savedPath;
    private String convertSavePath;
    private String thumbnailSavepath;
    private String convertSuffix;
    private String thumbnailSuffix;

    private final Map<Long,Integer> progressMap = new HashMap<>() ;


    public FFmpegProbeResult getProbeResult(String fileName) {
        FFmpegProbeResult ffmpegProbeResult;
        System.out.println("probeResult 파일이름:" + fileName);
        try {
            ffmpegProbeResult = ffProbe.probe(savedPath+fileName);

            System.out.println("비트레이트 : "+ffmpegProbeResult.getStreams().get(0).bit_rate);
            System.out.println("채널 : "+ffmpegProbeResult.getStreams().get(0).channels);
            System.out.println("코덱 명 : "+ffmpegProbeResult.getStreams().get(0).codec_name);
            System.out.println("코덱 유형 : "+ffmpegProbeResult.getStreams().get(0).codec_type);
            System.out.println("해상도(너비) : "+ffmpegProbeResult.getStreams().get(0).width);
            System.out.println("해상도(높이) : "+ffmpegProbeResult.getStreams().get(0).height);
            System.out.println("포맷(확장자) : "+ffmpegProbeResult.getFormat().size);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ffmpegProbeResult;
    }

    
    public String createThumbnail(String fileName){
        String thumbnailName = FilenameUtils.getBaseName(fileName) + thumbnailSuffix + ".gif";

        FFmpegBuilder builder = new FFmpegBuilder()	
				.overrideOutputFiles(true)	// output 파일을 덮어쓸 것인지 여부(false일 경우, output path에 해당 파일이 존재할 경우 예외 발생 - File 'C:/Users/Desktop/test.png' already exists. Exiting.)
                .setInput(savedPath+"/"+fileName)     					// 썸네일 이미지 추출에 사용할 영상 파일의 절대 경로
                .addExtraArgs("-ss", "00:00:01") 			// 영상에서 추출하고자 하는 시간 - 00:00:01은 1초를 의미 
                .addOutput(thumbnailSavepath +thumbnailName) 		// 저장 절대 경로(확장자 미 지정 시 예외 발생 - [NULL @ 000002cc1f9fa500] Unable to find a suitable output format for 'C:/Users/kterr/test')
                .setFrames(1)								
                .done();    											

		FFmpegExecutor executor = new FFmpegExecutor(ffMpeg, ffProbe);		// FFmpeg 명령어 실행을 위한 FFmpegExecutor 객체 생성
		executor.createJob(builder).run();		
        
        return thumbnailName;
    }

    public String resizeByWidth(Long videoId, String fileName, int resizedWidth){
        FFmpegProbeResult probeResult = getProbeResult(fileName);
        
        float aspectRatio = (float)probeResult.getStreams().get(0).width / probeResult.getStreams().get(0).height;
        int resizedHeight = Math.round(resizedWidth / aspectRatio);
        resizedHeight = (resizedHeight % 2 != 0) ? resizedHeight + 1 : resizedHeight;
        String outputFileName = FilenameUtils.getBaseName(fileName) + "_" + resizedWidth + "." + FilenameUtils.getExtension(fileName);
        FFmpegBuilder builder = new FFmpegBuilder()
                                    .setInput(probeResult)
                                    .overrideOutputFiles(true)
                                    .addOutput(convertSavePath + outputFileName)
                                    .setVideoResolution(resizedWidth, resizedHeight)
                                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                                    .done();

        FFmpegExecutor executable = new FFmpegExecutor(ffMpeg, ffProbe);
        FFmpegJob job = executable.createJob(builder, new ProgressListener() {

            // Using the FFmpegProbeResult determine the duration of the input
            final double durationNanosecs = probeResult.getFormat().duration * TimeUnit.SECONDS.toNanos(1);
        
            @Override
            public void progress(Progress progress) {
                int percentage = (int) (progress.out_time_ns / durationNanosecs * 100);
                
                if (percentage == 100) {
                    try{
                        Thread.sleep(3);
                        progressMap.remove(videoId);
                    }catch(InterruptedException e){
                        System.out.println("진행상태 삭제 오류");
                    }
                }
                progressMap.put(videoId, percentage);

                // Print out interesting information about the progress
                
                System.out.println(String.format(
                    "[%d] status:%s frame:%d time:%s ms fps:%.0f speed:%.2fx",
                    percentage,
                    progress.status,
                    progress.frame,
                    FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS),
                    progress.fps.doubleValue(),
                    progress.speed
                ));
            }
        });

        job.run();

        return outputFileName;
    }
}
