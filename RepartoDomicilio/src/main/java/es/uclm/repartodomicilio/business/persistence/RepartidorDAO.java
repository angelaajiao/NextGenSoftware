package es.uclm.repartodomicilio.business.persistence;

import es.uclm.repartodomicilio.business.entity.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepartidorDAO extends JpaRepository<Repartidor, Long> {
}
