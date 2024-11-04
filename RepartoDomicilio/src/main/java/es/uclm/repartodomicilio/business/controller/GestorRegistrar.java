/*package es.uclm.repartodomicilio.business.controller;
import es.uclm.repartodomicilio.business.entity.Greeting;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.uclm.repartodomicilio.business.entity.Register;
@Controller
public class GestorRegistrar {
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("register", new Register());
        return "Registro";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute Register register, Model model) {
        model.addAttribute("register", register);
        return "Registrado";
    }
}
*/