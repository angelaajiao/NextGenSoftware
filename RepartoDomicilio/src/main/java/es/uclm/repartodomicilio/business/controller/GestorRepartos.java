package es.uclm.repartodomicilio.business.controller;

import  es.uclm.repartodomicilio.business.entity.Repartidor;
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

    private  static  final Logger log = LoggerFactory.getLogger(GestorRepartos.class);

    @Autowired
    private  RepartidorDAO repartidorDAO;

    @GetMapping("/registro/repartidor")
    public String registroForm(Model model) {
            model.addAttribute("repartidor", new Repartidor());
            log.info(repartidorDAO.findAll().toString());
            return "Registro";
        }

        @PostMapping("/registro/repartidor")
    public String registroSubmit(@ModelAttribute Repartidor repartidor, Model model) {
        model.addAttribute("repartidor", repartidor);
        Repartidor savedRepartidor = repartidorDAO.save(repartidor);
        log.info("Saved greeting: " + savedRepartidor);
        return "result";
        }
}
