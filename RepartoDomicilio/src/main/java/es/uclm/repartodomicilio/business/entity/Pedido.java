package es.uclm.repartodomicilio.business.entity;
import java.util.*;

public class Pedido {
    Cliente cliente;
    Pago pago;
    Collection<ItemMenu> items;
    Restaurante restaurante;
    ServicioEntrega entrega;
    EstadoPedido estado;
    private Date fecha;

    /**
     *
     * @param itemMenu
     */
    public void add(iItemMenu itemMenu) {
        // TODO - implement Pedido.add
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param itemMenu
     */
    public void delete(ItemMenu itemMenu) {
        // TODO - implement Pedido.delete
        throw new UnsupportedOperationException();
    }
}
