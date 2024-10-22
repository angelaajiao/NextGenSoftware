package es.uclm.repartodomicilio.business.entity;

import jakarta.persistence.*;
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idUsuario;
    @Column
    private String pass;
    @Column
    private int attribute; //restaurante, cliente, repartidor

    public Usuario(){}
    // Creamos el constructor
    public Usuario(String pass, int attribute) {
        super();
        this.pass = pass;
        this.attribute = attribute;
    }

    // Métodos get y set
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return String.format("Usuario [idUsuario=%s, pass=%s, attribute=%s]", idUsuario, pass, attribute);
    }
}
