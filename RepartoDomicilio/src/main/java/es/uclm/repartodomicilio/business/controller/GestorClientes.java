package es.uclm.repartodomicilio.business.controller;


import es.uclm.repartodomicilio.business.entity.Cliente;
import es.uclm.repartodomicilio.business.entity.Restaurante;
import es.uclm.repartodomicilio.business.persistence.ClienteDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import es.uclm.repartodomicilio.business.persistence.RestauranteDAO;

import java.util.List;

@Controller
public class GestorClientes {

    private final ClienteDAO clienteDAO;
    private final RestauranteDAO restauranteDAO;
    public static final String STRING_CLIENTE = "cliente";
    public static final String STRING_RESTAURANTE = "restaurantes";

    public GestorClientes(ClienteDAO clienteDAO, RestauranteDAO restauranteDAO) {
        this.clienteDAO = clienteDAO;
        this.restauranteDAO = restauranteDAO;
    }

    @GetMapping("/registro/cliente")
    public String registroCliente(Model model) {
        model.addAttribute(STRING_CLIENTE, new Cliente());
        return "registroCliente";
    }

    @PostMapping("/registro/cliente")
    public String registrarCliente(@ModelAttribute Cliente cliente, Model model) {
        String errorMessage = null;

        // Verificamos si ya existe un cliente con el mismo DNI o email
        if (clienteDAO.existsByDni(cliente.getDni())) {
            errorMessage = "El DNI ya está registrado.";
        } else if (clienteDAO.existsByEmail(cliente.getEmail())) {
            errorMessage = "El correo electrónico ya está registrado.";
        }

        if (errorMessage != null) {
            model.addAttribute(STRING_CLIENTE, cliente); // Mantener los datos del formulario
            model.addAttribute("error", errorMessage);
            return "registroCliente"; // Volver al formulario con el mensaje de error
        }

        // Guardamos el cliente
        Cliente savedCliente = clienteDAO.save(cliente);
        model.addAttribute(STRING_CLIENTE, savedCliente);
        return "resultCliente";
    }


    @GetMapping("/")
    public String inicio(Model model) {
        List<Restaurante> restaurantes = restauranteDAO.findAll();
        model.addAttribute(STRING_RESTAURANTE, restaurantes);
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
        return listarRestaurantes(model); // Reutilizar el método
    }

    @PostMapping("/Cliente")
    public String listarRestaurantes(Model model) {
        List<Restaurante> restaurantes = restauranteDAO.findAll();
        model.addAttribute(STRING_RESTAURANTE, restaurantes);
        return "VistaCliente"; // Nombre de la vista donde se mostrará la lista
    }




}

