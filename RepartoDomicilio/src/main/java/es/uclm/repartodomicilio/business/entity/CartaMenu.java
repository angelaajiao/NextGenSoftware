package es.uclm.repartodomicilio.business.entity;
//la carta del menú que pertenece a un restaurante y contiene varios ítems

import jakarta.persistence.*;
import java.util.List;

@Entity
public class CartaMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cartaMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemMenu> items;

    @OneToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ItemMenu> getItems() {
        return items;
    }

    public void setItems(List<ItemMenu> items) {
        this.items = items;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }
}