package es.uclm.repartodomicilio.business.controller;


import es.uclm.repartodomicilio.business.entity.Cliente;
import es.uclm.repartodomicilio.business.entity.Restaurante;
import es.uclm.repartodomicilio.business.persistence.ClienteDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import es.uclm.repartodomicilio.business.persistence.RestauranteDAO;

import java.util.List;

@Controller
public class GestorClientes {

    @Autowired
    private ClienteDAO clienteDAO; // Asegúrate de que este DAO esté bien definido

    @Autowired
    private HttpSession session;
    @Autowired
    private RestauranteDAO restauranteDAO;

    @GetMapping("/registro/cliente")
    public String RegistroCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
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
        model.addAttribute("cliente", savedCliente);
        return "registradoCliente";
    }


    @GetMapping("/")
    public String Inicio(Model model) {
        List<Restaurante> restaurantes = restauranteDAO.findAll();
        model.addAttribute("restaurantes", restaurantes);
        return "Inicio";
    }
    // Método para la vista de favoritos
    @GetMapping("cliente/favoritos")
    public String mostrarFavoritos() {
        return "VistaFavoritos";
    }

    @PostMapping("cliente/favoritos")
    public String mostrarFavs() {
        return "VistaFavoritos";
    }

    @GetMapping("/Cliente")
    public String mostrarCliente(Model model) {

        List<Restaurante> restaurantes = restauranteDAO.findAll(); // Obtener todos los restaurantes
        model.addAttribute("restaurantes", restaurantes);
        return "VistaCliente";
    }

    @PostMapping("/Cliente")
    public String listarRestaurantes(Model model) {
        List<Restaurante> restaurantes = restauranteDAO.findAll();
        model.addAttribute("restaurantes", restaurantes);
        return "VistaCliente"; // Nombre de la vista donde se mostrará la lista
    }



}

