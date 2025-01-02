package es.uclm.repartodomicilio.business.controller;

import es.uclm.repartodomicilio.business.entity.*;
import es.uclm.repartodomicilio.business.persistence.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pedido")
public class GestorPedidos {

    @Autowired
    private ItemMenuDAO itemMenuDAO;

    @Autowired
    private ClienteDAO clienteDAO;

    @Autowired
    private PedidoDAO pedidoDAO;

    @Autowired
    private HttpSession session;

    // Obtener o crear un pedido actual en la sesión
    private Pedido obtenerPedidoActual() {
        Pedido pedido = (Pedido) session.getAttribute("pedidoActual");
        if (pedido == null) {
            // Si no hay un pedido en la sesión, crear uno nuevo
            pedido = new Pedido();
            session.setAttribute("pedidoActual", pedido);
        }
        return pedido;
    }

    @GetMapping("/agregar/{itemId}")
    public String agregarItemAPedido(@PathVariable Long itemId, Model model) {
        // Obtener el ítem del menú
        ItemMenu item = itemMenuDAO.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Ítem no encontrado"));

        // Obtener el pedido actual de la sesión
        Pedido pedido = obtenerPedidoActual();

        // Obtener el cliente de la sesión (si está logueado)
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if (cliente != null) {
            // Asociar el cliente al pedido si no está ya asociado
            if (pedido.getCliente() == null) {
                pedido.setCliente(cliente);
            }
        } else {
            // Si no hay cliente en sesión, podrías redirigir o mostrar un mensaje
            model.addAttribute("error", "Debes iniciar sesión para realizar un pedido.");
            return "login";  // Redirigir al login
        }

        // Agregar el ítem al pedido actual
        pedido.getItems().add(item);

        // Actualizar el modelo con el pedido actual
        model.addAttribute("pedidoActual", pedido);
        model.addAttribute("mensaje", "Ítem agregado al pedido: " + item.getNombre());

        return "resumenPedido"; // Vista para revisar el pedido actual
    }

    @GetMapping("/carrito")
    public String verCarrito(Model model) {
        // Obtener el pedido actual de la sesión
        Pedido pedido = obtenerPedidoActual();

        // Mostrar el pedido en el carrito
        model.addAttribute("pedidoActual", pedido);
        return "carrito";  // Vista del carrito donde el cliente puede revisar sus ítems
    }

    @PostMapping("/confirmar")
    public String confirmarPedido(@RequestParam("dni") String dni, Model model) {
        // Obtener el cliente desde la sesión (se supone que ya está en la sesión)
        Cliente cliente = (Cliente) session.getAttribute("cliente");

        // Verificar si el cliente está presente en la sesión
        if (cliente == null) {
            // Si el cliente no está en la sesión, redirigir al login o mostrar un error
            return "redirect:/login";
        }

        // Obtener el pedido actual desde la sesión (suponiendo que ya está en la sesión)
        Pedido pedido = (Pedido) session.getAttribute("pedidoActual");

        // Verificar si el pedido está presente en la sesión
        if (pedido == null) {
            return "redirect:/carrito"; // Redirigir a la página del carrito si no hay un pedido
        }

        // Asociar el cliente al pedido
        pedido.setCliente(cliente);

        // Guardar el pedido en la base de datos
        pedidoDAO.save(pedido);

        // Limpiar el pedido actual de la sesión
        session.removeAttribute("pedidoActual");

        // Pasar los datos del pedido confirmado a la vista
        model.addAttribute("pedidoConfirmado", pedido);
        model.addAttribute("cliente", cliente);

        // Redirigir a la página de confirmación de pedido
        return "pedidoConfirmado";
    }
}
