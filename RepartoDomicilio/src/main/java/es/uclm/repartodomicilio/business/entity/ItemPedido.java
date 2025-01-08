package es.uclm.repartodomicilio.business.entity;

import jakarta.persistence.*;


@Entity
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_menu_id", nullable = false)
    private ItemMenu item;

    private int cantidad;

    public ItemPedido() {
    }

    public ItemPedido(ItemMenu item, int cantidad) {
        this.item = item;
        this.cantidad = cantidad;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemMenu getItem() {
        return item;
    }

    public void setItem(ItemMenu item) {
        this.item = item;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "ItemPedido{" +
                "id=" + id +
                ", item=" + item.getNombre() + // Asumiendo que ItemMenu tiene un campo nombre
                ", cantidad=" + cantidad +
                '}';
    }
}

