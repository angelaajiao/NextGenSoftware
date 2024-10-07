package es.uclm.repartodomicilio.business.entity;
import java.util.*;

public class Repartidor {
    Collection<ServicioEntrega> servicios;
    Collection<CodigoPostal> zonas;
    private String nombre;
    private String apellidos;
    private String nif;
    private int eficiencia;
}
