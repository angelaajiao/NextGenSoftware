package es.uclm.repartodomicilio.business.persistence;

import es.uclm.repartodomicilio.business.entity.ServicioEntrega;
import java.util.List;

public interface ServicioEntregaDAO {

    // Crear un nuevo servicio de entrega
    void create(ServicioEntrega servicioEntrega);

    // Obtener todos los servicios de entrega
    List<ServicioEntrega> findAll();

    // Obtener un servicio de entrega por ID
    ServicioEntrega findById(Long id);

    // Actualizar un servicio de entrega
    void update(ServicioEntrega servicioEntrega);

    // Eliminar un servicio de entrega
    void delete(ServicioEntrega servicioEntrega);
}

