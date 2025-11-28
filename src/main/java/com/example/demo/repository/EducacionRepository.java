package com.example.demo.repository;

import com.example.demo.model.Educacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducacionRepository extends JpaRepository<Educacion, Integer> {

    //cambiar a consulta @query de ser necesario
    List<Educacion> findAll();

}
