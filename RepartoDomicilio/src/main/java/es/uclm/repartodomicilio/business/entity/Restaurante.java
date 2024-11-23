package es.uclm.repartodomicilio.business.entity;
import jakarta.persistence.*;

@Entity
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cif;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String password_restaurante;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "carta_menu_id")
    private CartaMenu cartaMenu;

    //Constructor vacío requerido por JPA
    public Restaurante(){}

    // Constructor con parametros
    public Restaurante(String nombre, String cif, String password_restaurante, String direccion){
        this.nombre = nombre;
        this.cif = cif;
        this.password_restaurante = password_restaurante;
        this.direccion = direccion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPassword_restaurante() { return  password_restaurante; }

    public void setPassword_restaurante(String password_restaurante) {this.password_restaurante = password_restaurante;}

    public CartaMenu getCartaMenu() { return cartaMenu;}

    public void setCartaMenu(CartaMenu cartaMenu) { this.cartaMenu = cartaMenu;}

}

