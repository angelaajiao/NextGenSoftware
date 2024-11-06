package es.uclm.repartodomicilio.business.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.uclm.repartodomicilio.business.entity.Cliente;

import java.util.Optional;

@Repository
public interface ClienteDAO extends JpaRepository<Cliente, Long>{
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);

    Optional<Cliente> findByDni(String dni);


}
