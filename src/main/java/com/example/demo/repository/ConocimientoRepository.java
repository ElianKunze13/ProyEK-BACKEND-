package com.example.demo.repository;

import com.example.demo.model.Conocimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConocimientoRepository extends JpaRepository<Conocimiento, Integer> {
    //definir consultas JPQL para traer lista de conocimientos
    // segun su tipo (FRONTEND, BACKEND, BASE_DATOS, ETC.)
}
