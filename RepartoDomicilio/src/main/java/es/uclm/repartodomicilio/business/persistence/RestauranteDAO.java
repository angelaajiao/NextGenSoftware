package es.uclm.repartodomicilio.business.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.uclm.repartodomicilio.business.entity.Restaurante;

@Repository
public interface RestauranteDAO extends JpaRepository<Restaurante, Long>{
    boolean existsBycif(String cif); // para verificar si existe CIF
}
