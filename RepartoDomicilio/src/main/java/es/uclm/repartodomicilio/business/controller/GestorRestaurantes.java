package es.uclm.repartodomicilio.business.controller;
import es.uclm.repartodomicilio.business.persistence.ItemMenuDAO;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import es.uclm.repartodomicilio.business.entity.*;
import es.uclm.repartodomicilio.business.persistence.RestauranteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class GestorRestaurantes {
    @Autowired
    private RestauranteDAO restauranteDAO;

    @Autowired
    private ItemMenuDAO itemMenuDAO;

    @GetMapping("/registro/restaurante")
    public String registroRestaurante(Model model) {
        model.addAttribute("restaurante", new Restaurante());
        return "Restaurante";
    }

    @PostMapping("/registro/restaurante")
    public String registrarRestaurante(@ModelAttribute Restaurante restaurante, Model model) {
        if (restauranteDAO.existsBycif(restaurante.getCif())) {
            throw new IllegalArgumentException("El CIF ya existe en otro restaurante");
        }

        // Crear la carta de menú y asociarla al restaurante
        CartaMenu cartaMenu = new CartaMenu();
        cartaMenu.setRestaurante(restaurante); // Asociar el restaurante a la carta
        restaurante.setCartaMenu(cartaMenu);  // Asociar la carta al restaurante

        // Guardar el restaurante (y en cascada, la carta)
        restauranteDAO.save(restaurante);

        model.addAttribute("restaurante", restaurante);
        return "resultRestaurante";
    }
    @GetMapping("/restaurante")
    public String vistaRestaurante(@ModelAttribute Restaurante restaurante, Model model) {
        model.addAttribute("restaurante", restaurante);
        model.addAttribute("items", restaurante.getCartaMenu().getItems());
        model.addAttribute("isRestauranteLogueado", true);
        return "VistaRestaurante";
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
    @GetMapping("/restaurante/{id}/menu")
    public String verMenuRestauranteAnonimo(@PathVariable Long id, Model model) {
        Restaurante restaurante = restauranteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
        CartaMenu cartaMenu = restaurante.getCartaMenu();

        List<ItemMenu> items = (cartaMenu != null) ? cartaMenu.getItems() : Collections.emptyList();

        model.addAttribute("items", items);
        model.addAttribute("restaurante", restaurante);
        model.addAttribute("mensaje", items.isEmpty() ? "Este restaurante no tiene ítems disponibles." : "");

        return "verCartaMenuAnonimo";
    }

    // Agregar nuevo ítem al menú
    @GetMapping("/restaurante/{idRestaurante}/agregarItem")
    public String agregarItemMenu(@PathVariable Long idRestaurante, Model model) {
        Restaurante restaurante = restauranteDAO.findById(idRestaurante)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        List<TipoItemMenu> tipos = Arrays.asList(TipoItemMenu.values());
        CartaMenu cartaMenu = restaurante.getCartaMenu();

        model.addAttribute("nuevoItem", new ItemMenu());
        model.addAttribute("tipos", tipos);
        model.addAttribute("verMenuRestaurante", cartaMenu);
        return "agregarItemMenu";
    }

    @PostMapping("/restaurante/{idRestaurante}/agregarItem")
    public String agregarItemMenu(@PathVariable Long idRestaurante, @ModelAttribute ItemMenu nuevoItem, Model model) {
        if (nuevoItem.getNombre().trim().isEmpty()) {
            model.addAttribute("error", "El nombre del ítem no puede estar vacío.");
            return "agregarItemMenu";
        }

        if (nuevoItem.getPrecio() <= 0) {
            model.addAttribute("error", "El precio debe ser mayor que cero.");
            return "agregarItemMenu";
        }

        Restaurante restaurante = restauranteDAO.findById(idRestaurante)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        CartaMenu cartaMenu = restaurante.getCartaMenu();
        nuevoItem.setCartaMenu(cartaMenu);
        itemMenuDAO.save(nuevoItem);

        return "redirect:/restaurante/" + idRestaurante + "/verCartaMenu";
    }


    @PostMapping("/restaurante/{idRestaurante}/editarItem/{idItem}")
    public String guardarEdicionItemMenu(@PathVariable Long idRestaurante, @PathVariable Long idItem,
                                         @ModelAttribute ItemMenu itemEditado) {
        ItemMenu item = itemMenuDAO.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Ítem no encontrado"));

        item.setNombre(itemEditado.getNombre());
        item.setPrecio(itemEditado.getPrecio());
        item.setTipo(itemEditado.getTipo());

        itemMenuDAO.save(item);

        return "redirect:/restaurante/" + idRestaurante + "/verCartaMenu";
    }
}
