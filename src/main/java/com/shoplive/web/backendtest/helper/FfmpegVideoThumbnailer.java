package com.shoplive.web.backendtest.helper;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.shoplive.web.backendtest.interfaces.ProgressibleVideoThumbnailer;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.Progress.Status;
import net.bramp.ffmpeg.progress.ProgressListener;

@Component
public class FfmpegVideoThumbnailer extends FfmpegVideoHandler implements ProgressibleVideoThumbnailer{

    ProgressHandler progressHandler;

    public FfmpegVideoThumbnailer(FFmpeg ffMpeg, FFprobe ffProbe) {
        super(ffMpeg, ffProbe);
        this.progressHandler = new ProgressHandler();
    }

    // TODO: 확장자별 차이 (.gif, .png, .jpg), 적정 thumbnail 크기, 비율 등...
    @Override
    public void createThumbnail(String targetFilePath, String resultFilePath) {
        FFmpegProbeResult probeResult = getProbeResult(targetFilePath);

        FFmpegBuilder builder = new FFmpegBuilder()	
				.overrideOutputFiles(true)	
                .setInput(targetFilePath)     				
                .addExtraArgs("-ss", "00:00:01") 			
                .addOutput(resultFilePath) 		
                .setFrames(1)								
                .done();    											

		FFmpegExecutor executor = new FFmpegExecutor(this.getFfMpeg(), this.getFfProbe());		// FFmpeg 명령어 실행을 위한 FFmpegExecutor 객체 생성
		FFmpegJob job = executor.createJob(builder, new ProgressListener() {

            // Using the FFmpegProbeResult determine the duration of the input
            final double durationNanosecs = probeResult.getFormat().duration * TimeUnit.SECONDS.toNanos(1);
        
            @Override
            public void progress(Progress progress) {
                
                int percentage = 0;

                if (progress.status == Status.END) {           
                    percentage = 100;             
                    progressHandler.deleteProgress(targetFilePath);
                }
                else{
                    percentage = (int) Math.round(progress.out_time_ns / durationNanosecs * 100);
                } 

                progressHandler.updateProgress(targetFilePath, percentage);
                
            }
        });
        
        
        job.run();
                
    }

    @Override
    public Integer getProgress(String filePath){
        return progressHandler.getProgress(filePath);
    }

    @Override
    public void deleteProgress(String filePath) {
        
        progressHandler.deleteProgress(filePath);
    }

    @Override
    public void updateProgress(String filePath, Integer percentage) {

        progressHandler.updateProgress(filePath, percentage);
    }
    
}
