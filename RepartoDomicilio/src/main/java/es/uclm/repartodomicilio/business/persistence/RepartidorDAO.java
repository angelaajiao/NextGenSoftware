package es.uclm.repartodomicilio.business.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.uclm.repartodomicilio.business.entity.Repartidor;
import java.util.Optional;

@Repository
public interface RepartidorDAO  extends  JpaRepository<Repartidor, Long >{
    Optional<Repartidor> findByDniRepartidor(String dniRepartidor);
    boolean existsByDniRepartidor(String dni);  // Verificar si el DNI ya está registrado
    boolean existsByEmailRepartidor(String email);  // Verificar si el correo ya está registrado

}
