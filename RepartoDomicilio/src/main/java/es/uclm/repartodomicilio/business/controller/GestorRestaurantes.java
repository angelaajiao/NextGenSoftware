package es.uclm.repartodomicilio.business.controller;

import es.uclm.repartodomicilio.business.entity.*;
import es.uclm.repartodomicilio.business.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.List;

@Controller
public class GestorRestaurantes {

    @Autowired
    private RestauranteDAO restauranteDAO;

    @Autowired
    private CartaMenuDAO cartaMenuDAO;

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
        restauranteDAO.save(restaurante);

        // Crear y asociar la carta de menú vacía
        CartaMenu cartaMenu = new CartaMenu();
        cartaMenu.setRestaurante(restaurante);
        cartaMenuDAO.save(cartaMenu);

        restaurante.setCartaMenu(cartaMenu);
        restauranteDAO.save(restaurante);

        model.addAttribute("restaurante", restaurante);
        return "resultRestaurante";
    }



    @GetMapping("/restaurante")
    public String vistaRestaurante(@ModelAttribute Restaurante restaurante, Model model) {
        return "VistaRestaurante";
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("restaurante", new Restaurante());
        return "Restaurante"; // Volver al formulario con el error
    }

    @GetMapping("/listarRestaurantes")
    public String mostrarRestaurantesLogin(Model model) {
        List<Restaurante> restaurantes = restauranteDAO.findAll();
        model.addAttribute("restaurantes", restaurantes);
        return "listarRestaurantes";
    }


    @GetMapping("/restaurante/{id}/menu")
    public String verMenuRestaurante(@PathVariable Long id, Model model) {
        Restaurante restaurante = restauranteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
        CartaMenu cartaMenu = restaurante.getCartaMenu();
        model.addAttribute("restaurante", restaurante);
        model.addAttribute("verMenuRestaurante", cartaMenu.getItems()); // Cambiado a usar la instancia 'cartaMenu'
        return "verMenuRestaurante";
    }
    @GetMapping("/restaurante/{idRestaurante}/agregarItem")
    public String agregarItemMenu(@PathVariable Long idRestaurante, Model model) {
        Restaurante restaurante = restauranteDAO.findById(idRestaurante)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        // Obtener los tipos de ítems directamente desde el enum
        List<TipoItemMenu> tipos = Arrays.asList(TipoItemMenu.values());
        CartaMenu cartaMenu = restaurante.getCartaMenu();

        // Inicializar el objeto 'nuevoItem' correctamente
        model.addAttribute("nuevoItem", new ItemMenu());
        model.addAttribute("tipos", tipos);
        model.addAttribute("verMenuRestaurante", cartaMenu);  // Pasar la carta de menú asociada
        return "agregarItemMenu"; // Nombre de la vista que mostrará el formulario
    }

    // Procesar y guardar el ítem enviado desde el formulario
    @PostMapping("/restaurante/{id}/agregarItem")
    public String agregarItemMenu(@PathVariable Long id, @ModelAttribute ItemMenu nuevoItem,
                                  @RequestParam Long tipo, Model model) {
        // Validación manual
        if (nuevoItem.getNombre() == null || nuevoItem.getNombre().trim().isEmpty()) {
            model.addAttribute("error", "El nombre del ítem no puede estar vacío.");
            return "agregarItemMenu";  // Volver al formulario en caso de error
        }

        if (nuevoItem.getPrecio() <= 0) {
            model.addAttribute("error", "El precio debe ser mayor que cero.");
            return "agregarItemMenu";  // Volver al formulario en caso de error
        }

        // Recuperar el restaurante y la carta de menú asociada
        Restaurante restaurante = restauranteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
        CartaMenu cartaMenu = restaurante.getCartaMenu();

        // Asignar el tipo desde el enum (usando el índice)
        TipoItemMenu tipoItem = TipoItemMenu.values()[tipo.intValue()];  // Opción alternativa usando enum directo

        // Establecer los valores del ítem
        nuevoItem.setTipo(tipoItem);
        nuevoItem.setCartaMenu(cartaMenu);

        // Guardar el nuevo ítem en la base de datos
        itemMenuDAO.save(nuevoItem);

        // Redirigir al menú del restaurante después de agregar el ítem
        return "redirect:/restaurante/" + id + "/VistaRestaurante";
    }
}

