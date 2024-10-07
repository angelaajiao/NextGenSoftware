package es.uclm.repartodomicilio.business.entity;

import java.util.*;

public class Cliente {
    Collection<Restaurante> favoritos;
    Collection<Pedido> pedidos;
    Collection<Direccion> direcciones;
    private String nombre;
    private String apellidos;
    private String dni;
}
