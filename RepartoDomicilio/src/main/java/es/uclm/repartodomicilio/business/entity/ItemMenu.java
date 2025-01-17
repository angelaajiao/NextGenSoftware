package es.uclm.repartodomicilio.business.entity;

import jakarta.persistence.*;

@Entity
public class ItemMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private double precio;

    @Enumerated(EnumType.STRING)
    private TipoItemMenu tipo;

    @ManyToOne
    @JoinColumn(name = "carta_menu_id")
    private CartaMenu cartaMenu;


    // Constructor vacío requerido por JPA
    public ItemMenu() {}

    // Constructor
    public ItemMenu(String nombre, double precio, TipoItemMenu tipo, CartaMenu cartaMenu) {
        this.nombre = nombre;
        this.precio = precio;
        this.tipo = tipo;
        this.cartaMenu = cartaMenu;
    }


    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public TipoItemMenu getTipo() {
        return tipo;
    }

    public void setTipo(TipoItemMenu tipo) {
        this.tipo = tipo;
    }

    public CartaMenu getCartaMenu() {
        return cartaMenu;
    }

    public void setCartaMenu(CartaMenu cartaMenu) {
        this.cartaMenu = cartaMenu;
    }
}