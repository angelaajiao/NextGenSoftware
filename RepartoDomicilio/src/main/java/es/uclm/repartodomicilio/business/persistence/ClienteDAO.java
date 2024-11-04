package es.uclm.repartodomicilio.business.persistence;

import es.uclm.repartodomicilio.business.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteDAO extends JpaRepository<Cliente, Long> {

}
