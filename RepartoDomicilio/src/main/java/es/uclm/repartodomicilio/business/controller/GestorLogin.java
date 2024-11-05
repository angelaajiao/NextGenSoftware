package es.uclm.repartodomicilio.business.controller;

import es.uclm.repartodomicilio.business.entity.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class GestorLogin {

    @Autowired
    private Login loginService;

    @GetMapping("/login")
    public String mostrarlogin(Model model) {
        return "Login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String id, @RequestParam String pass, Model model) {
        String rol = loginService.autenticarUser(id, pass);

        if(rol != null){
            model.addAttribute("rol", rol);
            //Redirige a la vista correspondiente según el rol
            switch (rol){
                case "cliente":
                    return "vistaCliente"; // vista cliente
                case "repartidor":
                    return "vistaRepartidor"; // vista repartidor
                case "restaurante":
                    return "vistaRestaurante"; // vista restaurante
            }
        }
        model.addAttribute("error", "Clave o contraseñas incorrectos");
        return "login";
        }
    }
