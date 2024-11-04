package es.uclm.repartodomicilio.business.controller;

import  es.uclm.repartodomicilio.business.entity.Repartidor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import  org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        return "Repartidor";
    }
    @PostMapping("/registro/repartidor")
    public String registrarRepartidor(@ModelAttribute Repartidor repartidor, Model model) {
        // Guardamos el repartidor en la base de datos
        repartidorDAO.save(repartidor);
        model.addAttribute("repartidor", repartidor);
        return "result";
    }


}