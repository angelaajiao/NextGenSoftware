package es.uclm.repartodomicilio.business.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "servicios_entrega")
public class ServicioEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único para el servicio de entrega

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido; // Relación 1:1 con Pedido*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repartidor_id", nullable = true)
    private Repartidor repartidor; // Relación 0...* a 1 con Repartidor

    @Embedded
    private Direccion direccion; // Relación 1 a 1 con Direccion

    @Column(name = "fecha_recepcion", nullable = false)
    private LocalDateTime fechaRecepcion; // Fecha en que el sistema recibe el pedido

    @Column(name = "fecha_entrega", nullable = true)
    private LocalDateTime fechaEntrega; // Fecha en que el pedido es entregado

    // Constructor sin parámetros (requerido por JPA)
    public ServicioEntrega() {
    }

    // Constructor con parámetros
    public ServicioEntrega( Direccion direccion, LocalDateTime fechaRecepcion) {
        //this.pedido = pedido;
        this.direccion = direccion;
        this.fechaRecepcion = fechaRecepcion;
    }

    // Métodos getter y setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }*/

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    @Override
    public String toString() {
        return "ServicioEntrega{" +
                "id=" + id +
                //", pedido=" + pedido +
                ", repartidor=" + repartidor +
                ", direccion=" + direccion +
                ", fechaRecepcion=" + fechaRecepcion +
                ", fechaEntrega=" + fechaEntrega +
                '}';
    }
}
