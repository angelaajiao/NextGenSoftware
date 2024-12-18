package es.uclm.repartodomicilio.business.controller;


import es.uclm.repartodomicilio.business.entity.Cliente;
import es.uclm.repartodomicilio.business.persistence.ClienteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class GestorClientes {

    //private static final Logger log = LoggerFactory.getLogger(GestorClientes.class);

    @Autowired
    private ClienteDAO clienteDAO; // Asegúrate de que este DAO esté bien definido

    @GetMapping("/registro/cliente")
    public String RegistroCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        //log.info("Lista de clientes: {}", clienteDAO.findAll());
        return "registroCliente";
    }

    @PostMapping("/registro/cliente")
    public String registrarCliente(@ModelAttribute Cliente cliente, Model model) {
        //Verificamos si ya existe un cliente con el mismo DNI o correo
        if (clienteDAO.existsByDni(cliente.getDni())) {
            model.addAttribute("error", "El DNI ya está registrado.");
            model.addAttribute("cliente", cliente); // Mantener los datos del formulario
            return "registroCliente"; // Volver al formulario con el mensaje de error
        }

        if (clienteDAO.existsByEmail(cliente.getEmail())) {
            model.addAttribute("error", "El correo electrónico ya está registrado.");
            model.addAttribute("cliente", cliente); // Mantener los datos del formulario
            return "registroCliente"; // Volver al formulario con el mensaje de error
        }

        //Guardamos el cliente
        Cliente savedCliente = clienteDAO.save(cliente);
        //log.info("Cliente registrado: {}", savedCliente);
        model.addAttribute("cliente", savedCliente);
        return "registradoCliente";
    }
}

