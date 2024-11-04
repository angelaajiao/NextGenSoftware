package es.uclm.repartodomicilio.business.entity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="Restaurante")

public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos;*/

    /*@OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartaMenu> cartasMenu;*/

    @Embedded
    private String direccion;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String cif;

    //Constructor vacío requerido por JPA
    public Restaurante(){}

    // Constructor con parametros
    public Restaurante(String nombre, String cif, String direccion){
        this.nombre = nombre;
        this.cif = cif;
        this.direccion = direccion;
    }
    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public List<CartaMenu> getCartasMenu() {
        return cartasMenu;
    }

    public void setCartasMenu(List<CartaMenu> cartasMenu) {
        this.cartasMenu = cartasMenu;
    }
*/
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    /* Método para listar restaurante
     *
     * @param idRestaurante
     */
   /* public List<ItemMenu> listarMenu() {
        throw new UnsupportedOperationException();
    }*/

}

