package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j//dependencia usada para logs
public class ProyEkApplication {

	public static void main(String[] args) {

        SpringApplication.run(ProyEkApplication.class, args);
        log.info("Proyecto PORTOFOLIO iniciado... ");

    }

}
