package es.uclm.repartodomicilio.business.controller;

import  es.uclm.repartodomicilio.business.entity.Repartidor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import  org.springframework.beans.factory.annotation.Autowired;
import es.uclm.repartodomicilio.business.persistence.RepartidorDAO;

@Controller
public class GestorRepartos {

    private final RepartidorDAO repartidorDAO;
    public static final String STRING_REPARTIDOR = "repartidor";

    public GestorRepartos(RepartidorDAO repartidorDAO){
        this.repartidorDAO = repartidorDAO;
    }
    @GetMapping("/registro/repartidor")
    public String registroRepartidor(Model model) {
        model.addAttribute(STRING_REPARTIDOR, new Repartidor());
        return "registroRepartidor";
    }
    @PostMapping("/registro/repartidor")
    public String registrarRepartidor(@ModelAttribute Repartidor repartidor, Model model) {

        String errorMessage = null;
        // Verificamos si ya existe un repartidor con el mismo DNI
        if (repartidorDAO.existsByDniRepartidor(repartidor.getDniRepartidor())) {
            errorMessage = "Ya existe un repartidor con ese DNI";
        } else if (repartidorDAO.existsByEmailRepartidor(repartidor.getEmailRepartidor())) { // Verificamos si ya existe un repartidor con el mismo correo electrónico
            errorMessage = "Ya existe un repartidor con ese email";
        }

        if (errorMessage != null) {
            model.addAttribute(STRING_REPARTIDOR, repartidor);  // Mantener los datos del formulario
            model.addAttribute("error", errorMessage);
            return "registroRepartidor";  // Volver al formulario con el mensaje de error
        }

        // Guardamos el repartidor en la base de datos
        repartidorDAO.save(repartidor);
        model.addAttribute(STRING_REPARTIDOR, repartidor);
        return "resultRepartidor";
    }


}