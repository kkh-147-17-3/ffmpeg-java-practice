package com.shoplive.web.backendtest.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.shoplive.web.backendtest.dto.VideoDetailsResponse;
import com.shoplive.web.backendtest.entity.Video;

@Mapper(componentModel = "spring")
public interface VideoMapper {
    
    @Mapping(target="original.filesize", source="originalFilesize")
    @Mapping(target="original.width", source="originalWidth")
    @Mapping(target="original.height", source="originalHeight")
    @Mapping(target="original.videoUrl", source="originalVideoUrl")
    @Mapping(target="resized.filesize", source="resizedFilesize")
    @Mapping(target="resized.width", source="resizedWidth")
    @Mapping(target="resized.height", source="resizedHeight")
    @Mapping(target="resized.videoUrl", source="resizedVideoUrl")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "localDateTimeToZonedDateTime")
    VideoDetailsResponse getDetailsResponseFromEntity(Video video);

    @Named("localDateTimeToZonedDateTime")
    public static String localDateTimeToZonedDateTime(LocalDateTime ldt){
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime zdtAtSeoul = ldt.atZone(zoneId);
        return zdtAtSeoul.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
