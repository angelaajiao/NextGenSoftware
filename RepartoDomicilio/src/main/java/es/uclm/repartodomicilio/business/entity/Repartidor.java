package es.uclm.repartodomicilio.business.entity;

import jakarta.persistence.*;

@Entity

public class Repartidor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "dni_repartidor", nullable = false)
    private String dniRepartidor;

    @Column(nullable = false)
    private String nombreRepartidor;

    @Column(nullable = false)
    private String apellidoRepartidor;

    @Column(nullable = false)
    private String ContrasenaRepartidor;

    @Column(nullable = false, unique = true)
    private String emailRepartidor;

    @Column(nullable = false)
    private boolean disponible;
    
    //private int eficiencia;
    //  Collection<ServicioEntrega> servicios;
    //  Collection<CodigoPostal> zonas;

    // Constructor vacío
    public Repartidor() {
    }

    // Constructor
    public Repartidor(String dniRepartidor, String nombreRepartidor, String apellidoRepartidor, String passwordRepartidor, String emailRepartidor, boolean disponible) {
        this.dniRepartidor = dniRepartidor;
        this.nombreRepartidor = nombreRepartidor;
        this.apellidoRepartidor = apellidoRepartidor;
        this.ContrasenaRepartidor = passwordRepartidor;
        this.emailRepartidor = emailRepartidor;
        this.disponible = disponible;
    }

    //Métodos get y set
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDniRepartidor() {
        return dniRepartidor;
    }

    public void setDniRepartidor(String dniRepartidor) {
        this.dniRepartidor = dniRepartidor;
    }

    public String getNombreRepartidor() {
        return nombreRepartidor;
    }

    public void setNombreRepartidor(String nombreRepartidor) {
        this.nombreRepartidor = nombreRepartidor;
    }

    public String getApellidoRepartidor() {
        return apellidoRepartidor;
    }

    public void setApellidoRepartidor(String apellidoRepartidor) {
        this.apellidoRepartidor = apellidoRepartidor;
    }
    public String getContrasenaRepartidor(){return ContrasenaRepartidor; }

    public  void setContrasenaRepartidor(String contrasenaRepartidor) {

        this.ContrasenaRepartidor = contrasenaRepartidor;

    }

    public String getEmailRepartidor(){
        return  emailRepartidor;
    }

    public void setEmailRepartidor(String emailRepartidor){
        this.emailRepartidor = emailRepartidor;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}

