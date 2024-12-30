package es.uclm.repartodomicilio.business.controller;

import es.uclm.repartodomicilio.business.entity.Login;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class GestorLogin {

    private final Login loginService;

    // Constructor
    public GestorLogin(Login loginService){
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String mostrarlogin(Model model) {
        return "Login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String id, @RequestParam String pass, Model model, HttpSession session) {
        String rol = loginService.autenticarUser(id, pass);

        if (rol != null) {
            session.setAttribute("rolActivo", rol);
            model.addAttribute("rol", rol);
            //Redirige a la vista correspondiente según el rol
            switch (rol) {
                case "Cliente":
                    return "redirect:/Cliente"; // vista cliente
                case "Repartidor":
                    return "VistaRepartidor"; // vista repartidor
                case "Restaurante":
                    return "redirect:/restaurante/" + id + "/inicio"; // vista restaurante

                default:
                    throw new IllegalArgumentException("Rol desconocido: " + rol + "\nSeleccione Cliente, Repartidor o Restaurante");

            }
        }
        model.addAttribute("error", "Clave o contraseñas incorrectos");
        return "login";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }
}