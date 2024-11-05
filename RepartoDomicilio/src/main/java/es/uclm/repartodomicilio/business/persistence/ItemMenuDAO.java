package es.uclm.repartodomicilio.business.persistence;

import es.uclm.repartodomicilio.business.entity.ItemMenu;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ItemMenuDAO extends JpaRepository<ItemMenu, Long> {
}
