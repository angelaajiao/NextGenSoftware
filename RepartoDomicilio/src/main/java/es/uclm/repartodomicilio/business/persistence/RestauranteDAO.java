package es.uclm.repartodomicilio.business.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.uclm.repartodomicilio.business.entity.Restaurante;

import java.util.List;

public interface RestauranteDAO extends JpaRepository<Restaurante, Long> {
    // Método personalizado para encontrar un restaurante por nombre
    List<Restaurante> findByNombre(String nombre);
}
