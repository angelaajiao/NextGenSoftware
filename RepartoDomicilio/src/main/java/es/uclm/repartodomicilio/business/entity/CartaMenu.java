package es.uclm.repartodomicilio.business.entity;
//la carta del menú que pertenece a un restaurante y contiene varios ítems

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class CartaMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCarta;

    @OneToMany(mappedBy = "cartaMenu", cascade = CascadeType.ALL)
    private List<ItemMenu> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    // Constructor vacío
    public CartaMenu() {}

    //Constructor
    public CartaMenu(String nombreCarta, Restaurante restaurante) {
        this.nombreCarta = nombreCarta;
        this.restaurante = restaurante;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCarta() {
        return nombreCarta;
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