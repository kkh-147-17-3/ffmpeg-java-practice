package com.shoplive.web.backendtest.helper;

import org.springframework.stereotype.Component;

import com.shoplive.web.backendtest.interfaces.ProgressibleVideoThumbnailer;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

@Component
public class FfmpegThumbnailHandler extends FfmpegVideoHandler implements ProgressibleVideoThumbnailer{

    ProgressHandler progressHandler;

    public FfmpegThumbnailHandler(FFmpeg ffMpeg, FFprobe ffProbe) {
        super(ffMpeg, ffProbe);
        this.progressHandler = new ProgressHandler();
    }

    @Override
    public void createThumbnail(String targetFilePath, String resultFilePath) {
        System.out.println(targetFilePath);
        System.out.println(resultFilePath);
        FFmpegBuilder builder = new FFmpegBuilder()	
				.overrideOutputFiles(true)	// output 파일을 덮어쓸 것인지 여부(false일 경우, output path에 해당 파일이 존재할 경우 예외 발생 - File 'C:/Users/Desktop/test.png' already exists. Exiting.)
                .setInput(targetFilePath)     					// 썸네일 이미지 추출에 사용할 영상 파일의 절대 경로
                .addExtraArgs("-ss", "00:00:01") 			// 영상에서 추출하고자 하는 시간 - 00:00:01은 1초를 의미 
                .addOutput(resultFilePath) 		// 저장 절대 경로(확장자 미 지정 시 예외 발생 - [NULL @ 000002cc1f9fa500] Unable to find a suitable output format for 'C:/Users/kterr/test')
                .setFrames(1)								
                .done();    											

		FFmpegExecutor executor = new FFmpegExecutor(this.getFfMpeg(), this.getFfProbe());		// FFmpeg 명령어 실행을 위한 FFmpegExecutor 객체 생성
		executor.createJob(builder).run();		
                
    }

    @Override
    public Integer getProgress(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteProgress(String id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateProgress(String id, Integer percentage) {
        // TODO Auto-generated method stub
        
    }
    
}
