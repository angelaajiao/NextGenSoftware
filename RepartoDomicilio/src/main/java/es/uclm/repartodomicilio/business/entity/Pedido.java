package es.uclm.repartodomicilio.business.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_dni", referencedColumnName = "dni")
    private Cliente cliente; // Asociar el pedido con un cliente

    @ManyToMany
    @JoinTable(
            name = "pedido_items",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<ItemMenu> items = new ArrayList<>(); // Ítems del pedido

    private LocalDateTime fecha;

    // Constructor vacío
    public Pedido() {
        this.fecha = LocalDateTime.now();
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemMenu> getItems() {
        return items;
    }

    public void setItems(List<ItemMenu> items) {
        this.items = items;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setFechaPedido(LocalDateTime now) {
    }

    public void setCantidad(int cantidad) {
    }

    public void setCartaMenu(CartaMenu cartaMenu) {
    }

    public void setRestaurante(Restaurante restaurante) {

    }
}



