package es.uclm.repartodomicilio.business.persistence;

import es.uclm.repartodomicilio.business.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoDAO extends JpaRepository<Pedido, Long> {

    // Buscar todos los pedidos de un cliente por su DNI
    List<Pedido> findByClienteDni(String dni);

    // Buscar un pedido por su ID
    Pedido findById(long id);
}
