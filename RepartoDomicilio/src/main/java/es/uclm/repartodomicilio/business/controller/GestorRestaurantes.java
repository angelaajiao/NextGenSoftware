package es.uclm.repartodomicilio.business.controller;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import es.uclm.repartodomicilio.business.entity.*;
import es.uclm.repartodomicilio.business.persistence.RestauranteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import es.uclm.repartodomicilio.business.persistence.CartaMenuDAO;
import es.uclm.repartodomicilio.business.persistence.ItemMenuDAO;

import java.util.*;

@Controller
public class GestorRestaurantes {
    @Autowired
    private RestauranteDAO restauranteDAO;

    @Autowired
    private CartaMenuDAO cartaMenuDAO;

    @Autowired
    private ItemMenuDAO itemMenuDAO;

    @GetMapping("/registro/restaurante")
    public String RegistroRestaurante(Model model) {
        model.addAttribute("restaurante", new Restaurante());
        return "Restaurante";
    }

    @PostMapping("/registro/restaurante")
    public String registrarRestaurante(@ModelAttribute Restaurante restaurante, Model model) {
        //Verificamos si ya existe un restaurante con el mismo CIF
        if (restauranteDAO.existsBycif(restaurante.getCif())){
            throw new IllegalArgumentException("El CIF ya existe en otro restaurante");
        }
        // Guardamos el restaurante en la base de datos
        restauranteDAO.save(restaurante);

        // Crear una carta de menú vacía para el restaurante
        CartaMenu cartaMenu = new CartaMenu();
        cartaMenu.setRestaurante(restaurante);
        cartaMenuDAO.save(cartaMenu);

        // Asociar la carta al restaurante
        restaurante.setCartaMenu(cartaMenu);
        restauranteDAO.save(restaurante); // Guardamos el restaurante con la carta asociada

        model.addAttribute("restaurante", restaurante);
        return "resultRestaurante";
    }

    // Manejador de excepciones
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        model.addAttribute("error", e.getMessage()); // Pasamos el mensaje de error a la vista
        model.addAttribute("restaurante", new Restaurante()); // Para volver a cargar el formulario
        return "Restaurante"; // Volvemos a la página de registro con el mensaje de error
    }

    //Listar restaurantes desde usurio anonimo
    @GetMapping("/listarRestaurantes")
    public String mostrarRestaurantesLogin(Model model) {
        List<Restaurante> restaurantes = restauranteDAO.findAll();
        model.addAttribute("restaurantes", restaurantes);
        return "listarRestaurantes";
    }

    @PostMapping("/listarRestaurantes")
    public String listarRestaurantes(Model model) {
        List<Restaurante> restaurantes = restauranteDAO.findAll(); // Obtener todos los restaurantes
        model.addAttribute("restaurantes", restaurantes);
        return "listarRestaurantes"; // Nombre de la vista donde se mostrará la lista
    }

    // Método para buscar restaurantes por nombre
    @GetMapping("/AnonimoBuscarRestaurantes")
    public String buscarRestaurantes(@RequestParam String nombre, Model model) {
        List<Restaurante> restaurantes = restauranteDAO.findAllByNombre(nombre);
        model.addAttribute("restaurantes", restaurantes);
        return "listarRestaurantes"; //

    }
    // Asignar ítems a la carta del restaurante
    @GetMapping("/restaurante/{id}/agregarItem")
    public String agregarItemMenu(@PathVariable Long id, Model model) {
        Restaurante restaurante = restauranteDAO.findById(id).orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
        CartaMenu cartaMenu = restaurante.getCartaMenu();
        model.addAttribute("cartaMenu", cartaMenu);
        model.addAttribute("nuevoItem", new ItemMenu());
        return "agregarItemMenu"; // Vista para agregar nuevos ítems
    }

    @PostMapping("/restaurante/{id}/agregarItem")
    public String agregarItemMenu(@PathVariable Long id, @ModelAttribute ItemMenu nuevoItem, Model model) {
        Restaurante restaurante = restauranteDAO.findById(id).orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
        CartaMenu cartaMenu = restaurante.getCartaMenu();

        // Asignar el nuevo ítem a la carta del restaurante
        nuevoItem.setCartaMenu(cartaMenu);
        itemMenuDAO.save(nuevoItem);

        // Actualizar la carta con el nuevo ítem
        cartaMenu.getItems().add(nuevoItem);
        cartaMenuDAO.save(cartaMenu);

        model.addAttribute("cartaMenu", cartaMenu);
        return "resultItemMenu";  // Vista que confirma la adición del ítem
    }
}
