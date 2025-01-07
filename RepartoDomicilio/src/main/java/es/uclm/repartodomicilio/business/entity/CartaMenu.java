package es.uclm.repartodomicilio.business.entity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class CartaMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreCarta;

    @OneToMany(mappedBy = "cartaMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemMenu> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    //Constructor
    public CartaMenu() {}

    public CartaMenu(String nombreCarta, Restaurante restaurante) {
        this.nombreCarta = nombreCarta;
        this.restaurante = restaurante;
    }

    // Método para añadir un ítem al menú
    public void addItemMenu(ItemMenu itemMenu) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(itemMenu);
        itemMenu.setCartaMenu(this); // Establece la relación bidireccional
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

    public void setNombreCarta(String nombreCarta) {
        this.nombreCarta = nombreCarta;
    }
}