package es.uclm.repartodomicilio.business.controller;

import es.uclm.repartodomicilio.business.entity.*;
import es.uclm.repartodomicilio.business.persistence.*;
import jakarta.servlet.http.HttpSession;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pedido")
public class GestorPedidos {

    @Autowired
    private ItemMenuDAO itemMenuDAO;

    @Autowired
    private ClienteDAO clienteDAO;

    @Autowired
    private PedidoDAO pedidoDAO;


    // Lista temporal para almacenar los ítems seleccionados por el cliente
    private List<ItemMenu> pedidoActual = new ArrayList<>();

    @GetMapping("/agregar/{itemId}")
    public String agregarItemAPedido(@PathVariable Long itemId, Model model) {
        // Obtener el ítem del menú
        ItemMenu item = itemMenuDAO.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Ítem no encontrado"));

        // Agregar el ítem al pedido actual
        pedidoActual.add(item);

        // Mostrar la lista actualizada de ítems en el pedido
        model.addAttribute("pedidoActual", pedidoActual);
        model.addAttribute("mensaje", "Ítem agregado al pedido: " + item.getNombre());

        return "resumenPedido"; // Vista para revisar el pedido actual
    }

    @PostMapping("/confirmar")
    public String confirmarPedido(@RequestParam("dni") String dni, Model model) {
        // Buscar el cliente por su DNI
        Cliente cliente = clienteDAO.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con DNI: " + dni));

        // Crear un pedido con los ítems seleccionados
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setItems(new ArrayList<>(pedidoActual));

        // Guardar el pedido en la base de datos
        pedidoDAO.save(pedido);

        // Limpiar el pedido temporal
        pedidoActual.clear();

        // Pasar los datos del pedido confirmado a la vista
        model.addAttribute("pedidoConfirmado", pedido);
        model.addAttribute("cliente", cliente);

        return "pedidoConfirmado";
    }
}

