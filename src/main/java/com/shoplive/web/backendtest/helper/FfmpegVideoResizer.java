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
public class FfmpegVideoResizer extends FfmpegVideoHandler implements ProgressibleVideoResizer{

    ProgressHandler progressHandler;

    public FfmpegVideoResizer(FFmpeg ffmpeg, FFprobe ffprobe){
        super(ffmpeg, ffprobe);
        progressHandler = new ProgressHandler();
    }

    @Override
    public void resizeWidth(String targetFilePath, String resultFilePath, int resizeWidth) {
        FFmpegProbeResult probeResult = getProbeResult(targetFilePath);
        
        float aspectRatio = (float)probeResult.getStreams().get(0).width / probeResult.getStreams().get(0).height;
        
        int resizeHeight = Math.round(resizeWidth / aspectRatio);
        
        // resize 시 height가 홀수인 경우 작업이 진행되지 않으므로 홀수 height에는 1을 더해 짝수로 맞춰준다.
        // https://stackoverflow.com/questions/20847674/ffmpeg-libx264-height-not-divisible-by-2
        resizeHeight = (resizeHeight % 2 != 0) ? resizeHeight + 1 : resizeHeight;

        //https://superuser.com/questions/714804/converting-video-from-1080p-to-720p-with-smallest-quality-loss-using-ffmpeg
        FFmpegBuilder builder = new FFmpegBuilder()
                                    .setInput(probeResult)
                                    .overrideOutputFiles(true)
                                    .addOutput(resultFilePath)
                                    // aspect ratio를 유지하면서 width 기준으로 resizing
                                    .setVideoFilter(String.format("scale=%d:-2", resizeWidth))
                                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                                    // crf: 0에 가까울 수록 손실이 적으나, 통상적으로 18 이하에서는 시각적으로 확연한 차이가 없다고 함
                                    .setConstantRateFactor(18)
                                    // preset: ultrafast ~ veryslow 까지 선택할 수 있고, 빠를 수록 동일 품질 대비 용량이 크다.
                                    .setPreset("slower")
                                    .done();

        FFmpegExecutor executable = new FFmpegExecutor(this.getFfMpeg(), this.getFfProbe());
        FFmpegJob job = executable.createJob(builder, new ProgressListener() {

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
