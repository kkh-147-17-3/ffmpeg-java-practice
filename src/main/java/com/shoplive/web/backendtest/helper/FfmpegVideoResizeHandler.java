package com.shoplive.web.backendtest.helper;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.shoplive.web.backendtest.interfaces.ProgressibleVideoResizer;

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
public class FfmpegVideoResizeHandler extends FfmpegVideoHandler implements ProgressibleVideoResizer{

    ProgressHandler progressHandler;

    public FfmpegVideoResizeHandler(FFmpeg ffmpeg, FFprobe ffprobe){
        super(ffmpeg, ffprobe);
        progressHandler = new ProgressHandler();
    }

    @Override
    public void resizeWidth(String targetFilePath, String resultFilePath, int resizeWidth) {
        FFmpegProbeResult probeResult = getProbeResult(targetFilePath);
        
        float aspectRatio = (float)probeResult.getStreams().get(0).width / probeResult.getStreams().get(0).height;
        int resizeHeight = Math.round(resizeWidth / aspectRatio);
        resizeHeight = (resizeHeight % 2 != 0) ? resizeHeight + 1 : resizeHeight;

        FFmpegBuilder builder = new FFmpegBuilder()
                                    .setInput(probeResult)
                                    .overrideOutputFiles(true)
                                    .addOutput(resultFilePath)
                                    .setVideoResolution(resizeWidth, resizeHeight)
                                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                                    .done();

        FFmpegExecutor executable = new FFmpegExecutor(this.getFfMpeg(), this.getFfProbe());
        FFmpegJob job = executable.createJob(builder, new ProgressListener() {

            // Using the FFmpegProbeResult determine the duration of the input
            final double durationNanosecs = probeResult.getFormat().duration * TimeUnit.SECONDS.toNanos(1);
        
            @Override
            public void progress(Progress progress) {
                
                int percentage = 0;

                if (progress.status == Status.END) {                        
                    percentage = 100;
                }
                else{
                    percentage = (int) (progress.out_time_ns / durationNanosecs * 100);
                } 

                progressHandler.updateProgress(targetFilePath, percentage);
                

                // Print out interesting information about the progress
                
                System.out.println(String.format(
                    "[%d] status:%s frame:%d fps:%.0f speed:%.2fx",
                    percentage,
                    progress.status,
                    progress.frame,
                    progress.fps.doubleValue(),
                    progress.speed
                ));
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
