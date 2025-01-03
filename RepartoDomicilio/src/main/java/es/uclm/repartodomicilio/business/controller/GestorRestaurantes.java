package es.uclm.repartodomicilio.business.controller;
import es.uclm.repartodomicilio.business.persistence.CartaMenuDAO;
import es.uclm.repartodomicilio.business.persistence.ItemMenuDAO;
import jakarta.transaction.Transactional;
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

    //Obtener los datos de la carta
    private Map<String, Object> obtenerDatosCarta(Long restauranteId, Long cartaId) {
        Restaurante restaurante = restauranteDAO.findById(restauranteId)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        CartaMenu cartaMenu = cartaMenuDAO.findById(cartaId)
                .orElseThrow(() -> new RuntimeException("Carta de menú no encontrada"));

        if (!cartaMenu.getRestaurante().getId().equals(restauranteId)) {
            throw new RuntimeException("La carta de menú no pertenece al restaurante indicado.");
        }

        List<ItemMenu> items = cartaMenu.getItems();

        Map<String, Object> datos = new HashMap<>();
        datos.put("restaurante", restaurante);
        datos.put("cartaMenu", cartaMenu);
        datos.put("items", items);

        return datos;
    }

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

       // Guardar el restaurante
        restauranteDAO.save(restaurante);

        model.addAttribute("restaurante", restaurante);
        return "resultRestaurante";
    }

    @GetMapping("/restaurante")
    public String vistaRestaurante(@ModelAttribute Restaurante restaurante, Model model) {
        model.addAttribute("restaurante", restaurante);

        // Obtenemos todas las cartas del restaurante
        List<CartaMenu> cartasMenu = restaurante.getCartasMenu();

        model.addAttribute("cartasMenu", cartasMenu); // Pasamos la lista de cartas al modelo

        /*// Si se desea, también se pueden agregar los ítems de todas las cartas combinadas
        List<ItemMenu> todosLosItems = cartasMenu.stream()
                .flatMap(carta -> carta.getItems().stream())
                .toList();
        model.addAttribute("todosLosItems", todosLosItems);*/
        model.addAttribute("isRestauranteLogueado", true);
        return "VistaRestaurante";
    }

    /*// Manejador de excepciones
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        model.addAttribute("error", e.getMessage()); // Pasamos el mensaje de error a la vista
        model.addAttribute("restaurante", new Restaurante()); // Para volver a cargar el formulario
        return "Restaurante"; // Volvemos a la página de registro con el mensaje de error
    }*/

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
        return "listarRestaurantes";

    }

    // Para que en usuario anónimo se vea la carta (esto está bien)
    @GetMapping("/restaurante/{id}/menu")
    public String verMenuRestauranteAnonimo(@PathVariable Long id, Model model) {
        Restaurante restaurante = restauranteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        System.out.println("Restaurante encontrado: " + restaurante.getNombre());

        List<CartaMenu> cartasMenu = restaurante.getCartasMenu();

        if (cartasMenu.isEmpty()) {
            System.out.println("Restaurante no tiene cartas de menú disponibles.");
            model.addAttribute("mensaje", "Este restaurante no tiene cartas de menú disponibles.");
            model.addAttribute("cartasMenu", Collections.emptyList());
        } else {
            System.out.println("Cartas de menú encontradas: " + cartasMenu.size());
            model.addAttribute("cartasMenu", cartasMenu);
            model.addAttribute("mensaje", "");
        }

        model.addAttribute("restaurante", restaurante);
        return "verCartaMenuAnonimo";
    }

    // Para poder ver los items de cada carta
    @GetMapping("/restaurante/{restauranteId}/menu/{cartaId}")
    public String verItemsDeCarta(@PathVariable Long restauranteId, @PathVariable Long cartaId, Model model) {
        Map<String, Object> datos = obtenerDatosCarta(restauranteId, cartaId);
        model.addAttribute("restaurante", datos.get("restaurante"));
        model.addAttribute("cartaMenu", datos.get("cartaMenu"));
        model.addAttribute("items", datos.get("items"));

        return "verItemsCarta";
    }

    @GetMapping("/anonimo/restaurante/{restauranteId}/menu/{cartaId}")
    public String verItemsDeCartaAnonimo(@PathVariable Long restauranteId, @PathVariable Long cartaId, Model model) {
        Map<String, Object> datos = obtenerDatosCarta(restauranteId, cartaId);
        model.addAttribute("restaurante", datos.get("restaurante"));
        model.addAttribute("cartaMenu", datos.get("cartaMenu"));
        model.addAttribute("items", datos.get("items"));

        return "verItemsCartaAnonimo";
    }




    // Para que salga la vista del restaurante
    @GetMapping("/restaurante/{id}/inicio")
    public String verCartaMenu(@PathVariable String id, Model model) {
        Restaurante restaurante = restauranteDAO.findBycif(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        model.addAttribute("restaurante", restaurante);

        // Obtener todas las cartas de menú del restaurante
        List<CartaMenu> cartasMenu = restaurante.getCartasMenu(); // Supongamos que un restaurante puede tener múltiples cartas
        if (cartasMenu != null && !cartasMenu.isEmpty()) {
            model.addAttribute("cartasMenu", cartasMenu);  // Mostrar todas las cartas de menú
        } else {
            model.addAttribute("cartasMenu", new ArrayList<>());  // Si no hay cartas, pasar una lista vacía
        }

        return "VistaRestaurante";
    }

    // Para añadir una carta menu
    @GetMapping("/restaurante/{cif}/agregarCartas")
    public String mostrarFormularioAgregarCartas(@PathVariable String cif, Model model) {
        // Buscar el restaurante por CIF
        Restaurante restaurante = restauranteDAO.findBycif(cif)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        // Crear un objeto CartaMenu inicializado
        CartaMenu carta = new CartaMenu();
        carta.setRestaurante(restaurante); // Asignar el restaurante
        carta.setItems(new ArrayList<>()); // Inicializar la lista de ítems

        // Añadir datos al modelo
        model.addAttribute("restaurante", restaurante);
        model.addAttribute("cartaMenu", carta); // Pasar el objeto CartaMenu al modelo

        return "agregarCartaMenu";
    }


    //Agregar la carta con los items
    @PostMapping("/restaurante/{cif}/agregarCartaMenu")
    public String agregarCartaMenu(@PathVariable String cif,
                                   @ModelAttribute CartaMenu cartaMenu,
                                   Model model) {
        // Buscar restaurante
        Restaurante restaurante = restauranteDAO.findBycif(cif)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        // Asociar restaurante con la carta
        cartaMenu.setRestaurante(restaurante);

        // Asociar ítems con la carta
        if (cartaMenu.getItems() != null) {
            for (ItemMenu item : cartaMenu.getItems()) {
                item.setCartaMenu(cartaMenu);
            }
        }

        // Guardar carta (cascada guardará los ítems)
        cartaMenuDAO.save(cartaMenu);

        return "redirect:/restaurante/" + cif + "/inicio";
    }


    @GetMapping("/restaurante/{id}/agregarItem")
    public String agregarItemMenu(@PathVariable String id, Model model) {
        // Obtener restaurante
        Restaurante restaurante = restauranteDAO.findBycif(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        // Obtener la lista de cartas de menú del restaurante (en lugar de un solo objeto)
        List<CartaMenu> cartasMenu = restaurante.getCartasMenu(); // Aquí es una lista, no un solo objeto

        if (cartasMenu.isEmpty()) {
            // Si no hay cartas de menú, crear una nueva carta
            CartaMenu nuevaCarta = new CartaMenu();
            nuevaCarta.setRestaurante(restaurante);
            cartasMenu.add(cartaMenuDAO.save(nuevaCarta)); // Guardamos la nueva carta
        }

        // Añadir los atributos necesarios para la vista
        model.addAttribute("restaurante", restaurante);
        model.addAttribute("nuevoItem", new ItemMenu());
        model.addAttribute("tipos", Arrays.asList(TipoItemMenu.values()));
        model.addAttribute("cartasMenu", cartasMenu); // Ahora mostramos la lista de cartas

        return "agregarItemMenu";
    }


    @PostMapping("/restaurante/{id}/agregarItem")
    public String agregarItemMenu(@PathVariable String id,
                                  @RequestParam Long cartaId, // El ID de la carta seleccionada
                                  @ModelAttribute ItemMenu nuevoItem, Model model) {
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
        Restaurante restaurante = restauranteDAO.findBycif(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        // Obtener la carta de menú seleccionada por ID
        CartaMenu cartaMenu = cartaMenuDAO.findById(cartaId)
                .orElseThrow(() -> new RuntimeException("Carta de menú no encontrada"));

        // Relacionar el ítem con la carta del menú
        nuevoItem.setCartaMenu(cartaMenu);
        itemMenuDAO.save(nuevoItem); // Guardar el nuevo ítem

        return "redirect:/restaurante/" + id + "/inicio";  // Redirigir a la página del menú
    }



    @GetMapping("/restaurante/{id}/editarItem/{idItem}")
    public String mostrarFormularioEdicion(@PathVariable String id, @PathVariable Long idItem, Model model) {
        Restaurante restaurante = restauranteDAO.findBycif(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
        ItemMenu item = itemMenuDAO.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Ítem no encontrado"));

        model.addAttribute("restaurante", restaurante);  // Agregar el restaurante
        model.addAttribute("item", item);  // Pasar el ítem al modelo
        model.addAttribute("tipos", Arrays.asList(TipoItemMenu.values()));  // Opciones de tipos
        return "editarItemMenu";
    }


    @PostMapping("/restaurante/{id}/editarItem/{idItem}")
    public String guardarEdicionItemMenu(@PathVariable String id, @PathVariable Long idItem,
                                         @ModelAttribute ItemMenu itemEditado) {
        Restaurante restaurante = restauranteDAO.findBycif(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
        ItemMenu item = itemMenuDAO.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Ítem no encontrado"));

        // Actualizar los campos del ítem
        item.setNombre(itemEditado.getNombre());
        item.setPrecio(itemEditado.getPrecio());
        item.setTipo(itemEditado.getTipo());

        itemMenuDAO.save(item);  // Guardar el ítem actualizado

        return "redirect:/restaurante/" + restaurante.getCif() + "/inicio";
    }


    @PostMapping("/restaurante/{id}/eliminarItem/{idItem}")
    public String eliminarItemMenu(@PathVariable Long id, @PathVariable Long idItem) {
        ItemMenu item = itemMenuDAO.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Ítem no encontrado"));

        itemMenuDAO.delete(item);  // Eliminar el ítem del menú

        return "redirect:/restaurante/" + id + "/verCartaMenu";  // Redirigir a la página del menú
    }

}
