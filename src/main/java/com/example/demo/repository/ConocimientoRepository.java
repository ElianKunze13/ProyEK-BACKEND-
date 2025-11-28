package com.example.demo.repository;

import com.example.demo.model.Conocimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConocimientoRepository extends JpaRepository<Conocimiento, Integer> {
    //definir consultas para traer lista de conocimientos
    // segun su tipo (FRONTEND, BACKEND, BASE_DATOS, ETC.)

    @Query("SELECT c FROM Conocimiento r WHERE c.tipoConocimiento == FRONTEND ") // llama y filtar los reportes que no esten cerrados
    List<Conocimiento> findByFrontEnd();

    @Query("SELECT c FROM Conocimiento r WHERE c.tipoConocimiento == BACKEND ") // llama y filtar los reportes que no esten cerrados
    List<Conocimiento> findByBackend();

    @Query("SELECT c FROM Conocimiento r WHERE c.tipoConocimiento == BASE_DATOS ") // llama y filtar los reportes que no esten cerrados
    List<Conocimiento> findByBD();

    @Query("SELECT c FROM Conocimiento r WHERE c.tipoConocimiento == TESTING ") // llama y filtar los reportes que no esten cerrados
    List<Conocimiento> findByTest();

    @Query("SELECT c FROM Conocimiento r WHERE c.tipoConocimiento == OTROS ") // llama y filtar los reportes que no esten cerrados
    List<Conocimiento> findByOtros();

}
