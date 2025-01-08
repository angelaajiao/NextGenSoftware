package es.uclm.repartodomicilio.business.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import es.uclm.repartodomicilio.business.entity.Cliente;
import es.uclm.repartodomicilio.business.entity.Restaurante;
import es.uclm.repartodomicilio.business.persistence.ClienteDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import es.uclm.repartodomicilio.business.persistence.RestauranteDAO;

import java.util.List;
import java.util.stream.Collectors;

class ClienteNoEncontradoException extends RuntimeException {
    public ClienteNoEncontradoException(String message) {
        super(message);
    }
}
@Controller
public class GestorClientes {

    private final ClienteDAO clienteDAO;
    private final RestauranteDAO restauranteDAO;
    public static final String STRING_CLIENTE = "cliente";
    public static final String STRING_RESTAURANTE = "restaurantes";
    // Creamos logger para evitar usar System.out.println()
    private static final Logger logger = LoggerFactory.getLogger(GestorClientes.class);

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
    @GetMapping("/cliente/{id}/favoritos")
    public String mostrarFavoritos(@PathVariable Long id, Model model) {
        Cliente cliente = clienteDAO.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException("Cliente no encontrado"));

        // Asegurarse de cargar los favoritos
        List<Restaurante> favoritos = cliente.getFavoritos();
        model.addAttribute("cliente", cliente);
        model.addAttribute("favoritos", favoritos);

        return "VistaFavoritos";
    }

    /*@PostMapping("cliente/{id}/favoritos")
    public String mostrarFavs(@PathVariable Long id, Model model) {
        Cliente cliente = clienteDAO.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException("No hemos encontrado el cliente"));
        logger.info("Cliente encontrado: {}", cliente.getNombre());
        model.addAttribute(STRING_CLIENTE, cliente);

        return "VistaFavoritos";
    }*/

    @PostMapping("/cliente/{id}/favoritos/agregar")
    public String agregarFavorito(@PathVariable Long id, @RequestParam Long restauranteId) {
        Cliente cliente = clienteDAO.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException("Cliente no encontrado"));

        Restaurante restaurante = restauranteDAO.findById(restauranteId)
                .orElseThrow(() -> new RestauranteNoEncontradoException("Restaurante no encontrado"));

        if (!cliente.getFavoritos().contains(restaurante)) {
            cliente.getFavoritos().add(restaurante);
            clienteDAO.save(cliente);
        }

        return "redirect:/Cliente/" + cliente.getDni();
    }

    @PostMapping("/cliente/{id}/favoritos/eliminar")
    public String eliminarFavorito(@PathVariable Long id, @RequestParam Long restauranteId) {
        Cliente cliente = clienteDAO.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException("Cliente no encontrado"));

        Restaurante restaurante = restauranteDAO.findById(restauranteId)
                .orElseThrow(() -> new RestauranteNoEncontradoException("Restaurante no encontrado"));

        cliente.getFavoritos().remove(restaurante);
        clienteDAO.save(cliente);

        return "redirect:/Cliente/" + cliente.getDni();
    }


    @GetMapping("/Cliente/{id}")
    public String mostrarCliente(@PathVariable String id, Model model) {
        // Buscar cliente por ID o DNI
        Cliente cliente = clienteDAO.findByDni(id) // Si estás usando DNI como identificador
                .orElseThrow(() -> new ClienteNoEncontradoException("Cliente no encontrado con ID: " + id));

        List<Restaurante> restaurantes = restauranteDAO.findAll();
        List<Restaurante> favoritos = cliente.getFavoritos();

        // Log para verificar los favoritos del cliente
        logger.info("Favoritos del cliente {}: {}", cliente.getId(), favoritos.stream()
                .map(Restaurante::getNombre) // Mostrar los nombres de los restaurantes favoritos
                .collect(Collectors.toList()));

        // Pasar los datos al modelo
        model.addAttribute("cliente", cliente);
        model.addAttribute("restaurantes", restaurantes);
        model.addAttribute("favoritos", favoritos);

        return "VistaCliente";
    }

}

