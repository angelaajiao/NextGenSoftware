package es.uclm.repartodomicilio.business.entity;
import java.util.*;
import jakarta.persistence.*;

@Entity
@Table(name="Repartidor")

public class Repartidor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String dni_repartidor;

    @Column(nullable=false)
    private String nombre_repartidor;

    @Column(nullable=false)
    private String apellido_repartidor;

    //private int eficiencia;
    //  Collection<ServicioEntrega> servicios;
    //  Collection<CodigoPostal> zonas;

    // Constructor vacío
    public Repartidor(){}

    // Constructor
    public Repartidor(String dni_repartidor, String nombre_repartidor, String apellido_repartidor) {
        this.dni_repartidor = dni_repartidor;
        this.nombre_repartidor = nombre_repartidor;
        this.apellido_repartidor = apellido_repartidor;
    }

    //Métodos get y set
    public String getDni_repartidor() {
        return dni_repartidor;
    }

    public void setDni_repartidor(String dni_repartidor) {
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



}
