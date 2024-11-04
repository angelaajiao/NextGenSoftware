package es.uclm.repartodomicilio.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GestorRegistrar {

    @GetMapping("/registro")
    public String registro() {
        return "Registro"; // Página de selección de tipo de usuario
    }
}
