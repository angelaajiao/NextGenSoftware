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

//    private static final Logger log = LoggerFactory.getLogger(GestorRepartos.class);

    @Autowired
    private RepartidorDAO repartidorDAO;

    @GetMapping("/registro/repartidor")
    public String registroRepartidor(Model model) {
        model.addAttribute("repartidor", new Repartidor());
       // log.info(repartidorDAO.findAll().toString());
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
            model.addAttribute("repartidor", repartidor);  // Mantener los datos del formulario
            model.addAttribute("error", errorMessage);
            return "registroRepartidor";  // Volver al formulario con el mensaje de error
        }

        // Guardamos el repartidor en la base de datos
        repartidorDAO.save(repartidor);
        model.addAttribute("repartidor", repartidor);
        return "resultRepartidor";
    }


}