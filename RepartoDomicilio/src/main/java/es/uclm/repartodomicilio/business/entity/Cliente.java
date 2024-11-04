package es.uclm.repartodomicilio.business.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dni;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column (nullable = false, unique = true)
    private String email;

   /* @Embedded
    private Direccion direccion;*/

    //Esto lo comento porque de momento solo haré el registro
    /*Collection<Restaurante> favoritos;
    Collection<Pedido> pedidos;
    Collection<Direccion> direcciones;*/

    // constructor vacío requerido por JPA
    public Cliente(){}

    //Constructor
    public Cliente(Long dni, String nombre, String apellidos, String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
    }

    //Métodos get y set
    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /* public Collection<Restaurante> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(Collection<Restaurante> favoritos) {
        this.favoritos = favoritos;
    }

    public Collection<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Collection<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public Collection<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(Collection<Direccion> direcciones) {
        this.direcciones = direcciones;
    }*/

}
