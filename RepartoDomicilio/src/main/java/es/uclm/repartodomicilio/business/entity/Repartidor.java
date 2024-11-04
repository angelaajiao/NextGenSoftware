package es.uclm.repartodomicilio.business.entity;
import java.util.*;
import jakarta.persistence.*;

@Entity
public class Repartidor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long dni_repartidor;

    @Column(nullable=false)
    private String nombre_repartidor;

    @Column(nullable=false)
    private String apellido_repartidor;

    @Column(nullable = false)
    private boolean disponible;
    //private int eficiencia;
    //  Collection<ServicioEntrega> servicios;
    //  Collection<CodigoPostal> zonas;

    // Constructor vacío
    public Repartidor(){}

    // Constructor
    public Repartidor(Long dni_repartidor, String nombre_repartidor, String apellido_repartidor, boolean disponible) {
        this.dni_repartidor = dni_repartidor;
        this.nombre_repartidor = nombre_repartidor;
        this.apellido_repartidor = apellido_repartidor;
        this.disponible = disponible;
    }

    //Métodos get y set
    public Long getDni_repartidor() {
        return dni_repartidor;
    }

    public void setDni_repartidor(Long dni_repartidor) {
        this.dni_repartidor = dni_repartidor;
    }

    public String getNombre_repartidor() {
        return nombre_repartidor;
    }

    public void setNombre_repartidor(String nombre_repartidor) {
        this.nombre_repartidor = nombre_repartidor;
    }

    public String getApellido_repartidor() {
        return apellido_repartidor;
    }

    public void setApellido_repartidor(String apellido_repartidor) {
        this.apellido_repartidor = apellido_repartidor;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }


}
