package com.example.demo.repository;

import com.example.demo.model.Habilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabilidadRepository extends JpaRepository<Habilidad, Integer> {

@Query("SELECT h FROM Habilidad h")
List<Habilidad> findByUsuarioId();

}
