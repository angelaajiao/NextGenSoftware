package es.uclm.repartodomicilio.business.controller;
import es.uclm.repartodomicilio.business.entity.*;
import es.uclm.repartodomicilio.business.persistence.RestauranteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class GestorRestaurantes {
    @Autowired
    private RestauranteDAO restauranteDAO;

    public Restaurante registrarRestaurante(String nombre, String cif, String d) {
        Restaurante restaurante = new Restaurante(nombre, cif, d);

        // Guardamos el restaurante en la base de datos
        return restauranteDAO.save(restaurante);
    }

}
