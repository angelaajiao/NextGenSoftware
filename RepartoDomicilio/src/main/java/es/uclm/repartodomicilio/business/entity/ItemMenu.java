package es.uclm.repartodomicilio.business.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "item_menu")
public class ItemMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único para la entidad

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoItemMenu tipo; // Enum para el tipo de item (COMIDA, BEBIDA, POSTRE)

    @Column(nullable = false)
    private String nombre; // Nombre del item de menú

    @Column(nullable = false)
    private double precio; // Precio del item de menú

    @ManyToOne
    @JoinColumn(name = "carta_menu_id", nullable = false)
    private CartaMenu cartaMenu; // Relación con CartaMenu

    // Constructor vacío necesario para JPA
    public ItemMenu() {}

    // Constructor con parámetros
    public ItemMenu(TipoItemMenu tipo, String nombre, double precio, CartaMenu cartaMenu) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.precio = precio;
        this.cartaMenu = cartaMenu;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoItemMenu getTipo() {
        return tipo;
    }

    public void setTipo(TipoItemMenu tipo) {
        this.tipo = tipo;
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

    public CartaMenu getCartaMenu() {
        return cartaMenu;
    }

    public void setCartaMenu(CartaMenu cartaMenu) {
        this.cartaMenu = cartaMenu;
    }

    // Métodos adicionales: equals y hashCode (opcional pero recomendado)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemMenu itemMenu = (ItemMenu) o;

        return id != null ? id.equals(itemMenu.id) : itemMenu.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
