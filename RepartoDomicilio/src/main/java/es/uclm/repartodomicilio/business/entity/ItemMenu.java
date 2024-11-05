package es.uclm.repartodomicilio.business.entity;
import jakarta.persistence.*;

@Entity
public class ItemMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único para la entidad

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private double precio;

    @Enumerated(EnumType.STRING)
    private TipoItemMenu tipoItemMenu;

    // Relacion con CartaMenu
    @ManyToOne
    @JoinColumn(name="Id_Menu", nullable = false)
    private CartaMenu menu;

    //Constructor vacío
    public ItemMenu() {}

    //Constructor
    public ItemMenu(String nombre, double precio, TipoItemMenu tipoItemMenu, CartaMenu menu) {
        this.nombre = nombre;
        this.precio = precio;
        this.tipoItemMenu = tipoItemMenu;
        this.menu = menu;
    }

    // Get y set
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

    public TipoItemMenu getTipoItemMenu() {
        return tipoItemMenu;
    }

    public void setTipoItemMenu(TipoItemMenu tipoItemMenu) {
        this.tipoItemMenu = tipoItemMenu;
    }

    public CartaMenu getMenu() {
        return menu;
    }

    public void setMenu(CartaMenu menu) {
        this.menu = menu;
    }
}
