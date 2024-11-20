package es.uclm.repartodomicilio.business.entity;

import jakarta.persistence.*;

@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String dni;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private String contrasena;

    @Column (nullable = false, unique = true)
    private String email;

    @Column (nullable = false)
    private Direccion direccion;

    // constructor vacío requerido por JPA
    public Cliente(){}

    //Constructor
    public Cliente(String dni, String nombre, String apellidos, String contrasena,  String email, Direccion direccion) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.contrasena = contrasena;
        this.email = email;
        this.direccion = direccion;
    }

    //Métodos get y set


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getContrasena() { return  contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }


}
