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

    public Cliente registrarCliente(Long dni, String nombre, String apellidos, String email) {
        Cliente cliente = new Cliente(dni, nombre, apellidos, email);
        return clienteDAO.save(cliente);
    }

}
