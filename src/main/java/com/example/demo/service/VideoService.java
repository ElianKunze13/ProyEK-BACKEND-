package com.example.demo.service;

import com.example.demo.dto.VideoDto;

public interface VideoService {

    VideoDto saveVideo(VideoDto videoDto);
    void deleteVideoPorId(Integer id);
    VideoDto getVideoPorId(Integer id);
}
