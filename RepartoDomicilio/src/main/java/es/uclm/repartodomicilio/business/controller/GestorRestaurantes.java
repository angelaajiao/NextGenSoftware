package es.uclm.repartodomicilio.business.controller;
import es.uclm.repartodomicilio.business.persistence.CartaMenuDAO;
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

   //@Autowired
    //private CartaMenuDAO cartaMenuDAO;

    @Autowired
    private ItemMenuDAO itemMenuDAO;

    @GetMapping("/registro/restaurante")
    public String RegistroRestaurante(Model model) {
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
    public String verMenuRestaurante(@PathVariable Long id, Model model) {
        Restaurante restaurante = restauranteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        CartaMenu cartaMenu = restaurante.getCartaMenu();

        if (cartaMenu == null) {
            throw new RuntimeException("El restaurante no tiene una carta de menú asociada");
        }

        model.addAttribute("restaurante", restaurante);
        model.addAttribute("verMenuRestaurante", cartaMenu.getItems());
        return "verCartaMenu";
    }
    @GetMapping("/restaurante/{idRestaurante}/editarItem/{idItem}")
    public String editarItemMenu(@PathVariable Long idRestaurante, @PathVariable Long idItem, Model model) {
        Restaurante restaurante = restauranteDAO.findById(idRestaurante)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        ItemMenu item = itemMenuDAO.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Ítem no encontrado"));

        List<TipoItemMenu> tipos = Arrays.asList(TipoItemMenu.values());

        model.addAttribute("restaurante", restaurante);
        model.addAttribute("item", item);
        model.addAttribute("tipos", tipos);

        return "editarItemMenu"; // Crear una vista llamada editarItemMenu.html
    }
    @GetMapping("/restaurante/{idRestaurante}/agregarItem")
    public String agregarItemMenu(@PathVariable Long idRestaurante, Model model) {
        Restaurante restaurante = restauranteDAO.findById(idRestaurante)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        // Obtener los valores del enum directamente
        List<TipoItemMenu> tipos = Arrays.asList(TipoItemMenu.values());
        CartaMenu cartaMenu = restaurante.getCartaMenu();

        model.addAttribute("nuevoItem", new ItemMenu());
        model.addAttribute("tipos", tipos); // Pasamos los valores del enum a la vista
        model.addAttribute("verMenuRestaurante", cartaMenu);

        return "agregarItemMenu";
    }
    @PostMapping("/restaurante/{id}/agregarItem")
    public String agregarItemMenu(@PathVariable Long id, @ModelAttribute ItemMenu nuevoItem,
                                  Model model) {
        if (nuevoItem.getNombre() == null || nuevoItem.getNombre().trim().isEmpty()) {
            model.addAttribute("error", "El nombre del ítem no puede estar vacío.");
            return "agregarItemMenu";
        }

        if (nuevoItem.getPrecio() <= 0) {
            model.addAttribute("error", "El precio debe ser mayor que cero.");
            return "agregarItemMenu";
        }

        Restaurante restaurante = restauranteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
        CartaMenu cartaMenu = restaurante.getCartaMenu();

        // Establecer la carta de menú en el ítem
        nuevoItem.setCartaMenu(cartaMenu);

        // Guardar el ítem
        itemMenuDAO.save(nuevoItem);

        return "redirect:/restaurante/" + id + "/menu";
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

        return "redirect:/restaurante/" + idRestaurante + "/menu";
    }
    @GetMapping("/restaurante/{idRestaurante}/eliminarItem/{idItem}")
    public String eliminarItemMenu(@PathVariable Long idRestaurante, @PathVariable Long idItem) {
        itemMenuDAO.deleteById(idItem);
        return "redirect:/restaurante/" + idRestaurante + "/menu";
    }



}
