package es.uclm.repartodomicilio.business.controller;
import es.uclm.repartodomicilio.business.persistence.CartaMenuDAO;
import es.uclm.repartodomicilio.business.persistence.ItemMenuDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import es.uclm.repartodomicilio.business.entity.*;
import es.uclm.repartodomicilio.business.persistence.RestauranteDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
class RestauranteNoEncontradoException extends RuntimeException {
    public RestauranteNoEncontradoException(String message) {
        super(message);
    }
}

class CartaNoEncontradaException extends RuntimeException {
    public CartaNoEncontradaException(String message) {
        super(message);
    }
}

class CartaNoPerteneceException extends RuntimeException {
    public CartaNoPerteneceException(String message) {
        super(message);
    }
}

class ItemNoEncontradoException extends RuntimeException {
    public ItemNoEncontradoException(String message) {
        super(message);
    }
}

@Controller
public class GestorRestaurantes {
    private final RestauranteDAO restauranteDAO;

    private final CartaMenuDAO cartaMenuDAO;

    private final ItemMenuDAO itemMenuDAO;

    // constantes que necesitaremos
    private static final String ERROR_RESTAURANTE_NO_ENCONTRADO = "Restaurante no encontrado";
    private static final String ERROR_CARTA_NO_ENCONTRADA = "Carta de menú no encontrada";
    private static final String ERROR_CARTA_NO_PERTENECE = "La carta de menú no pertenece al restaurante indicado.";
    private static final String ERROR_ITEM_NO_ENCONTRADO = "Ítem no encontrado";
    public static final String STRING_RESTAURANTE = "restaurante";
    public static final String STRING_RESTAURANTES = "restaurantes";
    public static final String STRING_CARTAMENU = "cartaMenu";
    public static final String STRING_ITEMS = "items";
    private static final String VISTA_REGISTRO_RESTAURANTE = "registroRestaurante";
    private static final String VISTA_LISTAR_RESTAURANTES = "listarRestaurantes";
    private static final String REDIRECT_RESTAURANTE = "redirect:/restaurante/";
    public static final String STRING_ITEMS2 = "items[";


    // Creamos logger para evitar usar System.out.println()
    private static final Logger logger = LoggerFactory.getLogger(GestorRestaurantes.class);

    // Constructor para poder eliminar el Autowired
    public GestorRestaurantes(RestauranteDAO restauranteDAO, CartaMenuDAO cartaMenuDAO, ItemMenuDAO itemMenuDAO){
        this.restauranteDAO = restauranteDAO;
        this.cartaMenuDAO = cartaMenuDAO;
        this.itemMenuDAO = itemMenuDAO;
    }

    //Obtener los datos de la carta
    private Map<String, Object> obtenerDatosCarta(Long restauranteId, Long cartaId) {
        Restaurante restaurante = restauranteDAO.findById(restauranteId)
                .orElseThrow(() -> new RestauranteNoEncontradoException(ERROR_RESTAURANTE_NO_ENCONTRADO));

        CartaMenu cartaMenu = cartaMenuDAO.findById(cartaId)
                .orElseThrow(() -> new CartaNoEncontradaException(ERROR_CARTA_NO_ENCONTRADA));

        if (!cartaMenu.getRestaurante().getId().equals(restauranteId)) {
            throw new CartaNoPerteneceException(ERROR_CARTA_NO_PERTENECE);
        }

        List<ItemMenu> items = cartaMenu.getItems();

        Map<String, Object> datos = new HashMap<>();
        datos.put(STRING_RESTAURANTE, restaurante);
        datos.put(STRING_CARTAMENU, cartaMenu);
        datos.put(STRING_ITEMS, items);

        return datos;
    }

    @GetMapping("/registro/restaurante")
    public String registroRestaurante(Model model) {
        model.addAttribute(STRING_RESTAURANTE, new Restaurante());
        return VISTA_REGISTRO_RESTAURANTE;
    }

    @PostMapping("/registro/restaurante")
    public String registrarRestaurante(@ModelAttribute Restaurante restaurante, Model model) {
        try {
            // Verifica si ya existe un restaurante con ese CIF
            if (restauranteDAO.existsBycif(restaurante.getCif())) {
                // Si ya existe, se lanza una excepción
                model.addAttribute("error", "El CIF ya existe en otro Restaurante");
                return VISTA_REGISTRO_RESTAURANTE;
            }

            // Si no existe, guarda el restaurante
            restauranteDAO.save(restaurante);
            model.addAttribute(STRING_RESTAURANTE, restaurante);
            return "resultRestaurante";
        } catch (Exception e) {
            // Manejo general de errores
            model.addAttribute("error", e.getMessage());
            return VISTA_REGISTRO_RESTAURANTE;
        }
    }

    @GetMapping("/restaurante")
    public String vistaRestaurante(@ModelAttribute Restaurante restaurante, Model model) {
        model.addAttribute(STRING_RESTAURANTE, restaurante);

        // Obtenemos todas las cartas del restaurante
        List<CartaMenu> cartasMenu = restaurante.getCartasMenu();

        model.addAttribute(STRING_CARTAMENU, cartasMenu); // Pasamos la lista de cartas al modelo

        model.addAttribute("isRestauranteLogueado", true);
        return "VistaRestaurante";
    }

    //Método  para listar
    private String listarRestaurantesComunes(Model model) {
        List<Restaurante> restaurantes = restauranteDAO.findAll();
        model.addAttribute(STRING_RESTAURANTES, restaurantes);
        return VISTA_LISTAR_RESTAURANTES;
    }

    //Listar restaurantes desde usurio anonimo
    @GetMapping("/listarRestaurantes")
    public String mostrarRestaurantesLogin(Model model) {
        return listarRestaurantesComunes(model);
    }

    @PostMapping("/listarRestaurantes")
    public String listarRestaurantes(Model model) {
        return listarRestaurantesComunes(model);
    }

    // Método para buscar restaurantes por nombre
    @GetMapping("/AnonimoBuscarRestaurantes")
    public String buscarRestaurantes(@RequestParam String nombre, Model model) {
        List<Restaurante> restaurantes = restauranteDAO.findAllByNombre(nombre);
        model.addAttribute(STRING_RESTAURANTES, restaurantes);
        return VISTA_LISTAR_RESTAURANTES;

    }

    // Para que en usuario anónimo se vea la carta
    @GetMapping("/restaurante/{id}/menu")
    public String verMenuRestauranteAnonimo(@PathVariable Long id, Model model) {
        Restaurante restaurante = restauranteDAO.findById(id)
                .orElseThrow(() -> new RestauranteNoEncontradoException(ERROR_RESTAURANTE_NO_ENCONTRADO));

        logger.info("Restaurante encontrado: {}", restaurante.getNombre());

        List<CartaMenu> cartasMenu = restaurante.getCartasMenu();

        if (cartasMenu.isEmpty()) {
            logger.warn("Restaurante no tiene cartas de menú disponibles.");
            model.addAttribute("mensaje", "Este restaurante no tiene cartas de menú disponibles.");
            model.addAttribute(STRING_CARTAMENU, Collections.emptyList());
        } else {
            logger.info("Cartas de menú encontradas: {}", cartasMenu.size());
            model.addAttribute(STRING_CARTAMENU, cartasMenu);
            model.addAttribute("mensaje", "");
        }

        model.addAttribute(STRING_RESTAURANTE, restaurante);
        return "verCartaMenuAnonimo";
    }

    // Para poder ver los items de cada carta
    @GetMapping("/restaurante/{restauranteId}/menu/{cartaId}")
    public String verItemsDeCarta(@PathVariable Long restauranteId, @PathVariable Long cartaId, Model model) {
        Map<String, Object> datos = obtenerDatosCarta(restauranteId, cartaId);
        model.addAttribute(STRING_RESTAURANTE, datos.get(STRING_RESTAURANTE));
        model.addAttribute(STRING_CARTAMENU, datos.get(STRING_CARTAMENU));
        model.addAttribute(STRING_ITEMS, datos.get(STRING_ITEMS));

        return "verItemsCartaRestaurante";
    }

    @GetMapping("/anonimo/restaurante/{restauranteId}/menu/{cartaId}")
    public String verItemsDeCartaAnonimo(@PathVariable Long restauranteId, @PathVariable Long cartaId, Model model) {
        Map<String, Object> datos = obtenerDatosCarta(restauranteId, cartaId);
        model.addAttribute(STRING_RESTAURANTE, datos.get(STRING_RESTAURANTE));
        model.addAttribute(STRING_CARTAMENU, datos.get(STRING_CARTAMENU));
        model.addAttribute(STRING_ITEMS, datos.get(STRING_ITEMS));

        return "verItemsCartaAnonimo";
    }


    // Para que salga la vista del restaurante
    @GetMapping("/restaurante/{id}/inicio")
    public String verCartaMenu(@PathVariable String id, Model model) {
        Restaurante restaurante = restauranteDAO.findBycif(id)
                .orElseThrow(() -> new RestauranteNoEncontradoException(ERROR_RESTAURANTE_NO_ENCONTRADO));

        model.addAttribute(STRING_RESTAURANTE, restaurante);

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
                .orElseThrow(() -> new RestauranteNoEncontradoException(ERROR_RESTAURANTE_NO_ENCONTRADO));

        // Crear un objeto CartaMenu inicializado
        CartaMenu carta = new CartaMenu();
        carta.setRestaurante(restaurante); // Asignar el restaurante

        // Añadir datos al modelo
        model.addAttribute(STRING_RESTAURANTE, restaurante);
        model.addAttribute(STRING_CARTAMENU, carta); // Pasar el objeto CartaMenu al modelo

        return "agregarCartaMenu";
    }


    //Agregar la carta con los items
    @PostMapping("/restaurante/{cif}/agregarCartaMenu")
    public String agregarCartaMenu(@PathVariable String cif,
                                   @ModelAttribute CartaMenu cartaMenu,
                                   Model model) {
        // Buscar restaurante
        Restaurante restaurante = restauranteDAO.findBycif(cif)
                .orElseThrow(() -> new RestauranteNoEncontradoException(ERROR_RESTAURANTE_NO_ENCONTRADO));

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

        return REDIRECT_RESTAURANTE + cif + "/inicio";
    }


    @GetMapping("/restaurante/{id}/editarItem/{idItem}")
    public String mostrarFormularioEdicion(@PathVariable String id, @PathVariable Long idItem, Model model) {
        Restaurante restaurante = restauranteDAO.findBycif(id)
                .orElseThrow(() -> new RestauranteNoEncontradoException(ERROR_RESTAURANTE_NO_ENCONTRADO));
        ItemMenu item = itemMenuDAO.findById(idItem)
                .orElseThrow(() -> new ItemNoEncontradoException(ERROR_ITEM_NO_ENCONTRADO));

        model.addAttribute(STRING_RESTAURANTE, restaurante);  // Agregar el restaurante
        model.addAttribute("item", item);  // Pasar el ítem al modelo
        model.addAttribute("tipos", Arrays.asList(TipoItemMenu.values()));  // Opciones de tipos
        return "editarItemMenu";
    }


    @PostMapping("/restaurante/{id}/editarItem/{idItem}")
    public String guardarEdicionItemMenu(@PathVariable String id, @PathVariable Long idItem,
                                         @ModelAttribute ItemMenu itemEditado) {
        Restaurante restaurante = restauranteDAO.findBycif(id)
                .orElseThrow(() -> new RestauranteNoEncontradoException(ERROR_RESTAURANTE_NO_ENCONTRADO));
        ItemMenu item = itemMenuDAO.findById(idItem)
                .orElseThrow(() -> new ItemNoEncontradoException(ERROR_ITEM_NO_ENCONTRADO));

        // Actualizar los campos del ítem
        item.setNombre(itemEditado.getNombre());
        item.setPrecio(itemEditado.getPrecio());
        item.setTipo(itemEditado.getTipo());

        itemMenuDAO.save(item);  // Guardar el ítem actualizado

        return REDIRECT_RESTAURANTE + restaurante.getCif() + "/inicio";
    }


    @PostMapping("/restaurante/{id}/eliminarItem/{idItem}")
    public String eliminarItemMenu(@PathVariable Long id, @PathVariable Long idItem) {
        ItemMenu item = itemMenuDAO.findById(idItem)
                .orElseThrow(() -> new ItemNoEncontradoException(ERROR_ITEM_NO_ENCONTRADO));

        itemMenuDAO.delete(item);  // Eliminar el ítem del menú

        return REDIRECT_RESTAURANTE + id + "verCartaMenuRestaurante";  // Redirigir a la página del menú
    }

    @GetMapping("/restaurante/{id}/agregarItem/{idcarta}")
    public String mostrarFormularioAgregarItem(@PathVariable Long id, @PathVariable Long idcarta, Model model) {
        Restaurante restaurante = restauranteDAO.findById(id)
                .orElseThrow(() -> new RestauranteNoEncontradoException(ERROR_RESTAURANTE_NO_ENCONTRADO));

        CartaMenu cartaMenu = cartaMenuDAO.findById(idcarta)
                .orElseThrow(() -> new CartaNoEncontradaException(ERROR_CARTA_NO_ENCONTRADA));

        model.addAttribute(STRING_RESTAURANTE, restaurante);
        model.addAttribute(STRING_CARTAMENU, cartaMenu);
        model.addAttribute("tipos", Arrays.asList(TipoItemMenu.values())); // Opciones para el tipo
        return "agregarItemMenu";
    }

    @PostMapping("/restaurante/{id}/agregarItem/{idcarta}")
    public String agregarItem(@PathVariable Long id,
                              @RequestParam Map<String, String> requestParams,
                              @PathVariable Long idcarta) {

        CartaMenu carta = cartaMenuDAO.findById(idcarta)
                .orElseThrow(() -> new CartaNoEncontradaException(ERROR_CARTA_NO_ENCONTRADA));

        //Se crea una lista a partir de los datos recibidos
        List<ItemMenu> items = new ArrayList<>();
        int index = 0;

        while (requestParams.containsKey(STRING_ITEMS2 + index + "].nombre")) {
            try {
                String nombre = requestParams.get(STRING_ITEMS2 + index + "].nombre");
                String precioStr = requestParams.get(STRING_ITEMS2 + index + "].precio");
                String tipoStr = requestParams.get(STRING_ITEMS2 + index + "].tipo");

                double precio = Double.parseDouble(precioStr);
                TipoItemMenu tipo = TipoItemMenu.valueOf(tipoStr.toUpperCase());

                // Crear un nuevo ítem
                ItemMenu item = new ItemMenu();
                item.setNombre(nombre);
                item.setPrecio(precio);
                item.setTipo(tipo);
                item.setCartaMenu(carta); // Establecer la relación con la carta

                items.add(item); // Agregar a la lista de ítems
            } catch (Exception e) {
                logger.error("Error al procesar ítem en índice {}: {}", index, e.getMessage(), e);
            }
            index++;
        }

        // Agregar los ítems a la carta
        for (ItemMenu item : items) {
            carta.addItemMenu(item);
        }

        // Guardar la carta y los ítems
        cartaMenuDAO.save(carta);

        return REDIRECT_RESTAURANTE + id + "/menu/" + idcarta;
    }

}
