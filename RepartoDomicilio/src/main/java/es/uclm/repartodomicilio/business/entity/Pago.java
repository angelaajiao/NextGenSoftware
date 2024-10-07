package es.uclm.repartodomicilio.business.entity;

public class Pago {
    Pedido pedido;
    MetodoPago tipo;
    private UUID idTransaccion;
    private Date fechaTransaccion;
}
