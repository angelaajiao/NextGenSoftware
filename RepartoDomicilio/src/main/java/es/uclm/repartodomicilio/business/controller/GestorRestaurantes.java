package es.uclm.repartodomicilio.business.controller;
import es.uclm.repartodomicilio.business.persistence.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import es.uclm.repartodomicilio.business.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class GestorRestaurantes {
    @Autowired
    private PedidoDAO pedidoDAO;

    @Autowired
    private RestauranteDAO restauranteDAO;

    @Autowired
    private CartaMenuDAO cartaMenuDAO;

    @Autowired
    private ItemMenuDAO itemMenuDAO;

    @Autowired
    private ClienteDAO clienteDAO;

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

    //Para encontrar el restaurante
    private Restaurante obtenerRestaurantePorId(Long id) {
        return restauranteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
    }

    // Para poder obtener las cartas
    private void configurarModeloConCartas(Long id, Model model, String mensajeSiVacio) {
        Restaurante restaurante = obtenerRestaurantePorId(id);
        List<CartaMenu> cartasMenu = restaurante.getCartasMenu();

        if (cartasMenu.isEmpty()) {
            model.addAttribute("mensaje", mensajeSiVacio);
            model.addAttribute("cartasMenu", Collections.emptyList());
        } else {
            model.addAttribute("mensaje", "");
            model.addAttribute("cartasMenu", cartasMenu);
        }

        model.addAttribute("restaurante", restaurante);
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

        model.addAttribute("isRestauranteLogueado", true);
        return "VistaRestaurante";
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
        return "listarRestaurantes";

    }

    // Para que en usuario anónimo se vea la carta
    @GetMapping("/restaurante/{id}/menu")
    public String verMenuRestauranteAnonimo(@PathVariable Long id, Model model) {
        configurarModeloConCartas(id, model, "Este restaurante no tiene cartas de menú disponibles.");
        return "verCartaMenuAnonimo";
    }

    // Para ver las cartas en el cliente
    @GetMapping("/Cliente/{clienteId}/restaurante/{restauranteId}/menu")
    public String verCartasCliente(@PathVariable Long clienteId, @PathVariable Long restauranteId, Model model) {
        configurarModeloConCartas(restauranteId, model, "Este restaurante no tiene cartas de menú disponibles.");
        Cliente cliente = clienteDAO.findById(clienteId)
                .orElseThrow(() -> new ClienteNoEncontradoException("No hemos encontrado el cliente"));
        model.addAttribute("cliente", cliente);


        return "VerCartaMenuCliente";
    }// revisar porque  sigue apareciendo el anterior link(la pagina si que va)


    // Para poder ver los items de cada carta
    @GetMapping("/restaurante/{restauranteId}/menu/{cartaId}")
    public String verItemsDeCarta(@PathVariable Long restauranteId, @PathVariable Long cartaId, Model model) {
        Map<String, Object> datos = obtenerDatosCarta(restauranteId, cartaId);
        model.addAttribute("restaurante", datos.get("restaurante"));
        model.addAttribute("cartaMenu", datos.get("cartaMenu"));
        model.addAttribute("items", datos.get("items"));

        return "verItemsCarta";
    }

    //Para anonimo
    @GetMapping("/anonimo/restaurante/{restauranteId}/menu/{cartaId}")
    public String verItemsDeCartaAnonimo(@PathVariable Long restauranteId, @PathVariable Long cartaId, Model model) {
        Map<String, Object> datos = obtenerDatosCarta(restauranteId, cartaId);
        model.addAttribute("restaurante", datos.get("restaurante"));
        model.addAttribute("cartaMenu", datos.get("cartaMenu"));
        model.addAttribute("items", datos.get("items"));

        return "verItemsCartaAnonimo";
    }

    // para el cliente
    @GetMapping("/Cliente/{clienteId}/restaurante/{restauranteId}/menu/{cartaId}")
    public String verItemsDeCartaCliente(@PathVariable Long clienteId, @PathVariable Long restauranteId, @PathVariable Long cartaId, Model model) {
        Map<String, Object> datos = obtenerDatosCarta(restauranteId, cartaId);
        model.addAttribute("restaurante", datos.get("restaurante"));
        model.addAttribute("cartaMenu", datos.get("cartaMenu"));
        model.addAttribute("items", datos.get("items"));
        model.addAttribute("clienteId", clienteId); // Agregar clienteId al modelo
        return "verItemsCartaCliente"; //funciona url
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
    @PostMapping("/Cliente/{clienteId}/restaurante/{restauranteId}/menu/{cartaId}/agregarPedido")
    public String agregarPedido(
            @PathVariable Long clienteId,
            @PathVariable Long restauranteId,
            @PathVariable Long cartaId,
            @RequestParam List<Long> itemIds,
            @RequestParam List<Integer> cantidades,
            Model model) {
        try {
            // Validar parámetros
            if (itemIds == null || itemIds.isEmpty()) {
                model.addAttribute("error", "Debe seleccionar al menos un ítem.");
                return "error"; // Vista de error amigable
            }

            if (cantidades == null || cantidades.size() != itemIds.size()) {
                model.addAttribute("error", "Debe proporcionar cantidades válidas para los ítems seleccionados.");
                return "error";
            }

            // Obtener cliente
            Cliente cliente = clienteDAO.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            // Obtener los datos de la carta y restaurante
            Map<String, Object> datos = obtenerDatosCarta(restauranteId, cartaId);
            Restaurante restaurante = (Restaurante) datos.get("restaurante");
            CartaMenu cartaMenu = (CartaMenu) datos.get("cartaMenu");
            List<ItemMenu> itemsDisponibles = (List<ItemMenu>) datos.get("items");

            model.addAttribute("items", itemsDisponibles);

            // Validar los ítems seleccionados
            List<ItemPedido> itemsPedido = new ArrayList<>();
            for (int i = 0; i < itemIds.size(); i++) {
                Long itemId = itemIds.get(i);
                int cantidad = cantidades.get(i);

                if (cantidad <= 0) {
                    throw new RuntimeException("Cantidad inválida para el ítem con ID: " + itemId);
                }

                ItemMenu item = itemsDisponibles.stream()
                        .filter(i -> i.getId().equals(itemId))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Ítem no encontrado: " + itemId));

                // Crear relación ítem-pedido
                ItemPedido itemPedido = new ItemPedido();
                itemPedido.setItem(item);
                itemPedido.setCantidad(cantidad);
                itemsPedido.add(itemPedido);
            }

            // Crear el pedido
            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setRestaurante(restaurante);
            pedido.setCartaMenu(cartaMenu);
            pedido.setItems(itemsPedido);
            pedido.setFechaPedido(LocalDateTime.now());

            // Guardar el pedido
            pedidoDAO.save(pedido);

            // Agregar al modelo para mostrar en la vista
            model.addAttribute("restaurante", restaurante);
            model.addAttribute("cartaMenu", cartaMenu);
            model.addAttribute("pedido", pedido);
            model.addAttribute("mensajeExito", "Pedido agregado con éxito.");
            model.addAttribute("cliente", cliente);

            return "resumenPedido";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }


}
