package es.uclm.repartodomicilio.business.controller;

import es.uclm.repartodomicilio.business.entity.Login;
import es.uclm.repartodomicilio.business.persistence.RestauranteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.uclm.repartodomicilio.business.entity.*;

import java.util.Optional;

@Controller
public class GestorLogin {

    private String rolActivo;

    @Autowired
    private Login loginService;
    @Autowired
    private RestauranteDAO restauranteDAO;

    @GetMapping("/login")
    public String mostrarlogin(Model model) {
        return "Login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String id, @RequestParam String pass, Model model) {
        String rol = loginService.autenticarUser(id, pass);

        if (rol != null) {
            rolActivo = rol;
            model.addAttribute("rol", rol);
            //Redirige a la vista correspondiente según el rol
            switch (rol) {
                case "Cliente":
                    return "vistaCliente"; // vista cliente
                case "Repartidor":
                    return "VistaRepartidor"; // vista repartidor
                case "Restaurante":
                    return "redirect:/restaurante/" + id + "/inicio";
            }
        }
        model.addAttribute("error", "Clave o contraseñas incorrectos");
        return "login";
    }


    @GetMapping("/logout")
    public String logout() {
        rolActivo = null;
        return "login";
    }


}