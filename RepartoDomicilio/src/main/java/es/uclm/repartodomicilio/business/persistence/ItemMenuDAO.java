package es.uclm.repartodomicilio.business.persistence;

import es.uclm.repartodomicilio.business.entity.ItemMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemMenuDAO extends JpaRepository<ItemMenu, Long> {
    @Query("SELECT i FROM ItemMenu i WHERE i.cartaMenu.restaurante.id = :restauranteId")
    List<ItemMenu> findByRestauranteId(@Param("restauranteId") Long restauranteId);

}


