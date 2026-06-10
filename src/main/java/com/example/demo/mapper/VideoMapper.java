package com.example.demo.mapper;

import com.example.demo.dto.VideoDto;
import com.example.demo.model.Video;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VideoMapper {
    VideoDto toVideoDto(Video video);
    Video toVideo(VideoDto videoDto);
}
