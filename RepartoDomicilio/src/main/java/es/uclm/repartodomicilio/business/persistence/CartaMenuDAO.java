package es.uclm.repartodomicilio.business.persistence;

import es.uclm.repartodomicilio.business.entity.CartaMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartaMenuDAO extends JpaRepository<CartaMenu, Long> {
}


