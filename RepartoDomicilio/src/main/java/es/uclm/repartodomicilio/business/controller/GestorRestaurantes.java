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

        if (restaurante.getCartaMenu() != null) {
            model.addAttribute("items", restaurante.getCartaMenu().getItems());
        } else {
            model.addAttribute("items", Collections.emptyList());
        }
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

        System.out.println("Restaurante encontrado: " + restaurante.getNombre());

        if(restaurante.getCartaMenu() != null){
            CartaMenu cartaMenu = restaurante.getCartaMenu();
            System.out.println("Carta del menú encontrada: " + (cartaMenu != null ? cartaMenu.getId() : "null"));

            List<ItemMenu> items = (cartaMenu != null && cartaMenu.getItems() != null)
                    ? cartaMenu.getItems()
                    : Collections.emptyList();

            // Log de cada ítem en el menú
            items.forEach(item -> System.out.println(
                    "nombre: " + item.getNombre() + ", precio: " + item.getPrecio() + ", tipo: " + item.getTipo()
            ));


            model.addAttribute("items", items);
            model.addAttribute("restaurante", restaurante);
            model.addAttribute("mensaje", items.isEmpty() ? "Este restaurante no tiene ítems disponibles." : "");
        }else{
            System.out.println("Restaurante no tiene un menú disponible");
        }


        return "verCartaMenuAnonimo";
    }

    @GetMapping("/restaurante/{id}/verCartaMenu")
    public String verCartaMenu(@PathVariable Long id, Model model) {
        Restaurante restaurante = restauranteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        model.addAttribute("restaurante", restaurante);
        // Obtener la carta de menú del restaurante
        CartaMenu cartaMenu = restaurante.getCartaMenu();
        if (cartaMenu != null && cartaMenu.getItems() != null) {

            model.addAttribute("items", cartaMenu.getItems());  // Mostrar los ítems asociados
        } else {
            model.addAttribute("items", new ArrayList<>());  // Si no hay ítems, pasar una lista vacía
        }

        return "verCartaMenu"; // Vista que muestra los ítems
    }



    @GetMapping("/restaurante/{id}/agregarItem")
    public String agregarItemMenu(@PathVariable Long id, Model model) {
        // Obtener restaurante
        Restaurante restaurante = restauranteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        // Verificar si el restaurante ya tiene una carta de menú
        CartaMenu cartaMenu = restaurante.getCartaMenu();
        if (cartaMenu == null) {
            // Si no existe una carta de menú para este restaurante, creamos una nueva
            cartaMenu = new CartaMenu();
            cartaMenu.setRestaurante(restaurante);
            cartaMenu = cartaMenuDAO.save(cartaMenu); // Guardamos la nueva carta
        }

        // Añadir los atributos necesarios para la vista
        model.addAttribute("restaurante", restaurante);
        model.addAttribute("nuevoItem", new ItemMenu());
        model.addAttribute("tipos", Arrays.asList(TipoItemMenu.values()));
        model.addAttribute("verMenuRestaurante", cartaMenu);  // Para mostrar la carta

        return "agregarItemMenu"; // Redirige a la página para agregar un ítem
    }


    @PostMapping("/restaurante/{id}/agregarItem")
    public String agregarItemMenu(@PathVariable Long id, @ModelAttribute ItemMenu nuevoItem, Model model) {
        // Validación de los campos del ítem
        if (nuevoItem.getNombre().trim().isEmpty()) {
            model.addAttribute("error", "El nombre del ítem no puede estar vacío.");
            return "agregarItemMenu";
        }

        if (nuevoItem.getPrecio() <= 0) {
            model.addAttribute("error", "El precio debe ser mayor que cero.");
            return "agregarItemMenu";
        }

        // Obtener restaurante
        Restaurante restaurante = restauranteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        // Obtener la carta de menú del restaurante
        CartaMenu cartaMenu = restaurante.getCartaMenu();
        if (cartaMenu == null) {
            // Si no existe una carta, crearla
            cartaMenu = new CartaMenu();
            cartaMenu.setRestaurante(restaurante);
            cartaMenu = cartaMenuDAO.save(cartaMenu);
        }

        // Relacionar el ítem con la carta del menú
        nuevoItem.setCartaMenu(cartaMenu);
        itemMenuDAO.save(nuevoItem); // Guardar el nuevo ítem

        return "redirect:/restaurante/" + id + "/verCartaMenu";  // Redirigir a la página del menú
    }
    @PostMapping("/restaurante/{id}/editarItem/{idItem}")
    public String guardarEdicionItemMenu(@PathVariable Long id, @PathVariable Long idItem,
                                         @ModelAttribute ItemMenu itemEditado) {
        ItemMenu item = itemMenuDAO.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Ítem no encontrado"));

        // Actualizar los campos del ítem
        item.setNombre(itemEditado.getNombre());
        item.setPrecio(itemEditado.getPrecio());
        item.setTipo(itemEditado.getTipo());

        itemMenuDAO.save(item);  // Guardar el ítem actualizado

        return "redirect:/restaurante/" + id + "/verCartaMenu";  // Redirigir a la página del menú
    }

    @PostMapping("/restaurante/{id}/eliminarItem/{idItem}")
    public String eliminarItemMenu(@PathVariable Long id, @PathVariable Long idItem) {
        ItemMenu item = itemMenuDAO.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Ítem no encontrado"));

        itemMenuDAO.delete(item);  // Eliminar el ítem del menú

        return "redirect:/restaurante/" + id + "/verCartaMenu";  // Redirigir a la página del menú
    }
}
