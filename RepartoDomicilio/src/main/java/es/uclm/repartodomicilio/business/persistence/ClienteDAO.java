package es.uclm.repartodomicilio.business.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.uclm.repartodomicilio.business.entity.Cliente;

import java.util.Optional;

@Repository
public interface ClienteDAO extends JpaRepository<Cliente, Long>{
    //boolean existsBydni(String dni); // para verificar si existe DNI

    Optional<Cliente> findByClaveUnica(String claveUnica);

}
