package es.uclm.repartodomicilio.business.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "Pagos")
public class Pago {

    @Id
    @Column(name = "IdTransacción" , nullable = false)
    private UUID idTransaccion;

    @Column(name ="fecha_Transacción" , nullable = false)
    private LocalDateTime fechaTransaccion;

    @Enumerated(EnumType.STRING)
    private MetodoPago tipo;

    /*@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    */

    // Constructor sin parámetros (requerido por JPA)
    public Pago() {
    }

    //Constructor con parámetros
    public Pago( UUID idTransaccion, LocalDateTime fechaTransaccion, MetodoPago tipo){
        this.idTransaccion = idTransaccion;
        this.fechaTransaccion = fechaTransaccion;
        this.tipo = tipo;
        //falta añadir atributo pedido

    }

    // Getters y Setters

    public UUID getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(UUID idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public LocalDateTime getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(LocalDateTime fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public MetodoPago getTipo() {
        return tipo;
    }

    public void setTipo(MetodoPago tipo) {
        this.tipo = tipo;
    }

    /*
    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    */


}
