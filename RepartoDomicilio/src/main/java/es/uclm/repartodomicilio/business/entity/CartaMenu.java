package es.uclm.repartodomicilio.business.entity;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class CartaMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @Column(nullable = false)
    private String nombre;


    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;


    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<ItemMenu> itemMenu;

    // Constructor, getters y setters
    public CartaMenu() {}

    public CartaMenu(String nombre, Restaurante restaurante) {
        this.nombre = nombre;
        this.restaurante = restaurante;
    }

    public long getMenuId() { return menuId; }
    public void setMenuId(Long menuId) { this.menuId = menuId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Restaurante getRestaurante() { return restaurante; }
    public void setRestaurante(Restaurante restaurante) { this.restaurante = restaurante; }

    public List<ItemMenu> getItemMenu() { return itemMenu; }
    public void setItemMenu(List<ItemMenu> itemMenu) { this.itemMenu = itemMenu; }
}