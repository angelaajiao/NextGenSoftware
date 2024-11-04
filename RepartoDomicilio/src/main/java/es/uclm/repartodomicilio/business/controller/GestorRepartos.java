package es.uclm.repartodomicilio.business.controller;

import es.uclm.repartodomicilio.business.entity.*;
import es.uclm.repartodomicilio.business.persistence.RepartidorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GestorRepartos {
    @Autowired
    private RepartidorDAO repartidorDAO;

    public Repartidor registrarRepartidos(String dni, String nombre, String apellido){
        Repartidor repartidor = new Repartidor(dni, nombre, apellido);
        return repartidorDAO.save(repartidor);
    }
    /*
     *
     * @param servicio
     */
  /*  public void marcarPedidoRecogido(ServicioEntrega servicio) {
        // TODO - implement GestorRepartos.marcarPedidoRecogido
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param servicio
     */
    /*public void marcarPedidoEntregado(ServicioEntrega servicio) {
        // TODO - implement GestorRepartos.marcarPedidoEntregado
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param nombre
     * @param apellidos
     * @param nif
     * @param zonas
     */
    /*public void registrarRepartidor(String nombre, String apellidos, String nif, List<CodigoPostal> zonas) {
        // TODO - implement GestorRepartos.registrarRepartidor
        throw new UnsupportedOperationException();
    }*/
}
