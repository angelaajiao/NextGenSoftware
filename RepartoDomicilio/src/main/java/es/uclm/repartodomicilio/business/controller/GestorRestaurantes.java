package es.uclm.repartodomicilio.business.controller;
import es.uclm.repartodomicilio.business.persistence.CartaMenuDAO;
import org.springframework.boot.Banner;
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
        //guardamos el restauranteid en la sesion
        model.addAttribute("restauranteId", restaurante.getId());
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

    @GetMapping("/VistaRestauranteAltaMenu")
    public String registroCartaMenu(Model model) {
        CartaMenu cartaMenu = new CartaMenu();
        List<ItemMenu> items = new ArrayList<>();
        items.add(new ItemMenu());
        cartaMenu.setItemMenu(items);
        model.addAttribute("cartaMenu", cartaMenu);
        model.addAttribute("tiposItemMenu", TipoItemMenu.values());

        return "VistaRestauranteAltaMenu";
    }


    //Dar del alta menu
    @PostMapping("/VistaRestauranteAltaMenu")
    public String altaCartaMenu(@ModelAttribute CartaMenu cartaMenu, Model model) {

        //Se recupera el restaurante usando el id
        Long restauranteid = (long) model.getAttribute("restauranteId");

        Optional<Restaurante> restaurante = restauranteDAO.findById(restauranteid);

        cartaMenu.setRestaurante(restaurante.get());

        //guarda la carta y los items del menu
        cartaMenu.getItemMenu().forEach(itemMenu -> itemMenu.setMenu(cartaMenu));
        cartaMenuDAO.save(cartaMenu);

        model.addAttribute("cartaMenu", cartaMenu);
        return "resultAltaMenu";//html

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
}
