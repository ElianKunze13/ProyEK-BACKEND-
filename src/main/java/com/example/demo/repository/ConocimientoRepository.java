package com.example.demo.repository;

import com.example.demo.enums.TipoConocimiento;
import com.example.demo.model.Conocimiento;
import com.example.demo.model.Educacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConocimientoRepository extends JpaRepository<Conocimiento, Integer> {

    List<Conocimiento> findAll();
    //definir consultas para traer lista de conocimientos
    // segun su tipo (FRONTEND, BACKEND, BASE_DATOS, ETC.)

    @Query("SELECT c FROM Conocimiento c LEFT JOIN FETCH c.imagenes WHERE c.tipoConocimiento = :tipoConocimiento ") // llama y filtar los reportes que no esten cerrados
    List<Conocimiento> findByFrontEnd(@Param("tipoConocimiento") TipoConocimiento tipoConocimiento);

    @Query("SELECT c FROM Conocimiento c WHERE c.tipoConocimiento = :tipoConocimiento") // llama y filtar los reportes que no esten cerrados
    List<Conocimiento> findByBackend(@Param("tipoConocimiento") TipoConocimiento tipoConocimiento);

    @Query("SELECT c FROM Conocimiento c WHERE c.tipoConocimiento = :tipoConocimiento ") // llama y filtar los reportes que no esten cerrados
    List<Conocimiento> findByBD(@Param("tipoConocimiento") TipoConocimiento tipoConocimiento);

    @Query("SELECT c FROM Conocimiento c WHERE c.tipoConocimiento = :tipoConocimiento ") // llama y filtar los reportes que no esten cerrados
    List<Conocimiento> findByTest(@Param("tipoConocimiento") TipoConocimiento tipoConocimiento);

    @Query("SELECT c FROM Conocimiento c WHERE c.tipoConocimiento = :tipoConocimiento ") // llama y filtar los reportes que no esten cerrados
    List<Conocimiento> findByOtros(@Param("tipoConocimiento") TipoConocimiento tipoConocimiento);

}
