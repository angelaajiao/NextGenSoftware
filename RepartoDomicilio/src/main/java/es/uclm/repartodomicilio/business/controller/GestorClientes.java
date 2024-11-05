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
        //Verificamos si ya existe un cliente con el mismo DNI
        /*if (ClienteDAO.existsBydni(cliente.getDni())){
            throw new IllegalArgumentException("El DNI ya existe en otro cliente");
        }*/

        //Guardamos el cliente
        Cliente savedCliente = clienteDAO.save(cliente);
        //log.info("Cliente registrado: {}", savedCliente);
        model.addAttribute("cliente", savedCliente);
        return "registradoCliente";
    }

    /*// Manejador de excepciones
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        model.addAttribute("error", e.getMessage()); // Pasamos el mensaje de error a la vista
        model.addAttribute("cliente", new Cliente()); // Para volver a cargar el formulario
        return "registroCliente"; // Volvemos a la página de registro con el mensaje de error
    }*/
}

