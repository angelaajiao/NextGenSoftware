package es.uclm.repartodomicilio.business.persistence;

import es.uclm.repartodomicilio.business.entity.ItemMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemMenuDAO extends JpaRepository<ItemMenu, Long> {

}

