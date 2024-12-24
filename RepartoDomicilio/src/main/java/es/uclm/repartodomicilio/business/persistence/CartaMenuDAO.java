package es.uclm.repartodomicilio.business.persistence;

import es.uclm.repartodomicilio.business.entity.CartaMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaMenuDAO extends JpaRepository<CartaMenu, Long> {
    // Método para encontrar una CartaMenu por el ID del restaurante
        CartaMenu findByRestauranteId(Long restauranteId);
}
