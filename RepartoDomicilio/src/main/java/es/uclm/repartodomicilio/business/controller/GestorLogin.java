package es.uclm.repartodomicilio.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class GestorLogin {

    @Autowired
    private Login loginService;

    @PostMapping
    public String login(@RequestParam String id, @RequestParam String pass) {
        String rol = loginService.autenticarUser(id, pass);
        if(rol != null){
            return "Login exitodo como: " + rol;
        } else {
            return "Clave o contraseña incorrectos";
        }
    }
}
