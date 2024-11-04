package es.uclm.repartodomicilio.business.controller;

import es.uclm.repartodomicilio.business.entity.Cliente;
import es.uclm.repartodomicilio.business.persistence.ClienteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Controller;

@Controller
public class GestorClientes {
    @Autowired
    private ClienteDAO clienteDAO;

    public Cliente registrarCliente(String dni, String nombre, String apellidos, String email) {
        Cliente cliente = new Cliente(dni, nombre, apellidos, email);
        return clienteDAO.save(cliente);
    }

    /*
     *
     * @param zona
     */
   /* public List<Restaurante> buscarRestaurante(CodigoPostal zona) {
        // TODO - implement GestorClientes.buscarRestaurante
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param cadenaBusqueda
     */
  /*  public List<Restaurante> buscarRestaurante(String cadenaBusqueda) {
        // TODO - implement GestorClientes.buscarRestaurante
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param cliente
     * @param r
     */
    /*public void favorito(Cliente cliente, Restaurante r) {
        // TODO - implement GestorClientes.favorito
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param nombre
     * @param apellido
     * @param d
     */
   /* public Cliente registrarCliente(String nombre, String apellido, Direccion d) {
        // TODO - implement GestorClientes.registrarCliente
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param calle
     * @param numeero
     * @param complemento
     * @param cp
     * @param municipio
     */
   /* private Direccion altaDirecion(String calle, String numeero, String complemento, CodigoPostal cp, String municipio) {
        // TODO - implement GestorClientes.altaDirecion
        throw new UnsupportedOperationException();
    }*/


}
