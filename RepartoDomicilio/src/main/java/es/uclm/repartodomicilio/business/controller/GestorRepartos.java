package es.uclm.repartodomicilio.business.controller;

import es.uclm.repartodomicilio.business.entity.*;
import es.uclm.repartodomicilio.business.persistence.RepartidorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GestorRepartos {
    @Autowired
    private RepartidorDAO repartidorDAO;

    public Repartidor registrarRepartidor(Long dni, String nombre, String apellido, boolean disponible){
        Repartidor repartidor = new Repartidor(dni, nombre, apellido, disponible);
        return repartidorDAO.save(repartidor);
    }

}
