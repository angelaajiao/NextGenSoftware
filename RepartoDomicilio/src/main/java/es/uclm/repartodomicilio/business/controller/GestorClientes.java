package es.uclm.repartodomicilio.business.controller;


import es.uclm.repartodomicilio.business.entity.Cliente;
import es.uclm.repartodomicilio.business.persistence.ClienteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class GestorClientes {

    private static final Logger log = LoggerFactory.getLogger(GestorClientes.class);

    @Autowired
    private ClienteDAO clienteDAO; // Asegúrate de que este DAO esté bien definido

    @GetMapping("/Clienteregister")
    public String clienteForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        log.info("Lista de clientes: {}", clienteDAO.findAll());
        return "registroCliente"; // Asegúrate de que esta vista exista
    }

    @PostMapping("/Clienteregister")
    public String clienteSubmit(@ModelAttribute Cliente cliente, Model model) {
        Cliente savedCliente = clienteDAO.save(cliente); // Asegúrate de usar la instancia correcta
        log.info("Cliente registrado: {}", savedCliente);
        model.addAttribute("cliente", savedCliente);
        return "registradoCliente"; // Asegúrate de que esta vista exista
    }
}

