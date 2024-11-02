package es.uclm.repartodomicilio.business.controller;
import es.uclm.repartodomicilio.business.entity.*;
import es.uclm.repartodomicilio.persistence.RestauranteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GestorRestaurantes {
    @Autowired
    private RestauranteDAO restauranteDAO;

    /* Registrar restaurante
     * @param nombre
     * @param cif
     * @param d
     */
    public Restaurante registrarRestaurante(String nombre, String cif, Direccion d) {
        Restaurante restaurante = new Restaurante();
        restaurante.setNombre(nombre);
        restaurante.setCif(cif);
        restaurante.setDireccion(d);

        // Guardamos el restaurante en la base de datos
        return restauranteDAO.save(restaurante);
    }

    /*
     * Método para editar la carta del restaurante
     * @param nombre
     * @param items
     */
    public void editarCarta(String nombre, List<ItemMenu> items) {
        Optional<Restaurante> optionalRestaurante = restauranteDAO.findById(nombre);
        if(optionalRestaurante.isPresent()) {
            Restaurante restaurante = optionalRestaurante.get();
            restaurante.getCartasMenu().clear();
            restaurante.getCartasMenu().addAll(items);
            restauranteDAO.save(restaurante);
        } else{
            throw new IllegalArgumentException("Restaurante no encontrado");
        }
    }

    /*
     * Crear nuevo item de menu
     * @param nombre
     * @param precio
     * @param tipo
     */
    private ItemMenu crearItem(String nombre, double precio, TipoItemMenu tipo) {
        TipoItemMenu itemMenu = new TipoItemMenu();
        itemMenu.setNombre(nombre);
        itemMenu.setPrecio(precio);
        itemMenu.setTipo(tipo);
        return itemMenu;
    }
}
