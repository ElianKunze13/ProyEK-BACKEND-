package com.example.demo.service.Impl;

import com.example.demo.dto.VideoDto;
import com.example.demo.mapper.VideoMapper;
import com.example.demo.model.Video;
import com.example.demo.repository.VideoRepository;
import com.example.demo.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j // remplaza el  Sout por el log
@Service // indica que es un servicio
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;

    @Override
    public VideoDto saveVideo(VideoDto videoDto) {
        log.info("Guardando video: {}", videoDto);
        var video = videoMapper.toVideo(videoDto);
        var savedVideo = videoRepository.save(video);
        return videoMapper.toVideoDto(savedVideo);
    }
    @Override
    public void deleteVideoPorId(Integer id) {
        videoRepository.deleteById(id);
    }

    @Override
    public VideoDto getVideoPorId(Integer id) {
        log.info("Obteniendo video con ID: {}", id);
        var video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video no encontrado con id " + id));
        return videoMapper.toVideoDto(video);
    }

}
