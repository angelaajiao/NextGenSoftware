package es.uclm.repartodomicilio.business.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.uclm.repartodomicilio.business.entity.Repartidor;
import java.util.Optional;

@Repository
public interface RepartidorDAO  extends  JpaRepository<Repartidor, Long >{
    Optional<Repartidor> findByClaveUnica(String claveUnica);

}
