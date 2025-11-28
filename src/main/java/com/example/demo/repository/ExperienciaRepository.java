package com.example.demo.repository;

import com.example.demo.model.Experiencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienciaRepository extends JpaRepository<Experiencia, Integer> {

    //cambiar a consulta @query de ser necesario

    List<Experiencia> findAll();
}
