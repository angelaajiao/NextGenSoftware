package es.uclm.repartodomicilio.business.controller;


import es.uclm.repartodomicilio.business.entity.Cliente;
import es.uclm.repartodomicilio.business.entity.Restaurante;
import es.uclm.repartodomicilio.business.persistence.ClienteDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import es.uclm.repartodomicilio.business.persistence.RestauranteDAO;

import java.util.List;

class ClienteNoEncontradoException extends RuntimeException {
    public ClienteNoEncontradoException(String message) {
        super(message);
    }
}

@Controller
public class GestorClientes {

    @Autowired
    private ClienteDAO clienteDAO; // Asegúrate de que este DAO esté bien definido

    @Autowired
    private HttpSession session;
    @Autowired
    private RestauranteDAO restauranteDAO;

    public static final String STRING_CLIENTE = "cliente";
    public static final String STRING_RESTAURANTE = "restaurantes";
    // Creamos logger para evitar usar System.out.println()
    private static final Logger logger = LoggerFactory.getLogger(GestorClientes.class);

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
    @GetMapping("cliente/{id}/favoritos")
    public String mostrarFavoritos(@PathVariable Long id, Model model) {
        Cliente cliente = clienteDAO.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException("No hemos encontrado el cliente"));
        model.addAttribute(STRING_CLIENTE, cliente);
        return "VistaFavoritos";
    }

    @PostMapping("cliente/{id}/favoritos")
    public String mostrarFavs(@PathVariable Long id, Model model) {
        Cliente cliente = clienteDAO.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException("No hemos encontrado el cliente"));
        logger.info("Cliente encontrado: {}", cliente.getNombre());
        model.addAttribute(STRING_CLIENTE, cliente);
        return "VistaFavoritos";
    }

    @GetMapping("/Cliente/{id}")
    public String mostrarCliente(@PathVariable String id, Model model) {

        // Buscar cliente por ID o DNI
        Cliente cliente = clienteDAO.findByDni(id) // Si estás usando DNI como identificador
                .orElseThrow(() -> new ClienteNoEncontradoException("Cliente no encontrado con ID: " + id));

        // Añadir atributos al modelo
        model.addAttribute(STRING_CLIENTE, cliente);

        List<Restaurante> restaurantes = restauranteDAO.findAll(); // Obtener todos los restaurantes
        model.addAttribute(STRING_RESTAURANTE, restaurantes);
        return "VistaCliente";
    }

    @PostMapping("/Cliente")
    public String listarRestaurantes(Model model) {
        List<Restaurante> restaurantes = restauranteDAO.findAll();
        model.addAttribute("restaurantes", restaurantes);
        return "VistaCliente"; // Nombre de la vista donde se mostrará la lista
    }



}

