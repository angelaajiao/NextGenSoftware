package es.uclm.repartodomicilio.business.entity;

import es.uclm.repartodomicilio.business.persistence.ClienteDAO;
import es.uclm.repartodomicilio.business.persistence.RepartidorDAO;
import es.uclm.repartodomicilio.business.persistence.RestauranteDAO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Login {
    private final ClienteDAO clienteDAO;
    private final RestauranteDAO restauranteDAO;
    private final RepartidorDAO repartidorDAO;

    public Login(ClienteDAO clienteDAO, RestauranteDAO restauranteDAO, RepartidorDAO repartidorDAO){
        this.clienteDAO = clienteDAO;
        this.restauranteDAO = restauranteDAO;
        this.repartidorDAO = repartidorDAO;
    }

    public String autenticarUser(String claveUnica, String contra){
        // VERIFICAR CLIENTE
        Optional<Cliente> cliente = clienteDAO.findByDni(claveUnica); // claveUnica debería ser el DNI
        if (cliente.isPresent() && cliente.get().getContrasena().equals(contra)){
            return "Cliente";
        }

        // VERIFICAR REPARTIDOR
        Optional<Repartidor> repartidor = repartidorDAO.findByDniRepartidor(claveUnica);
        if (repartidor.isPresent() && repartidor.get().getContrasenaRepartidor().equals(contra)) {
            return "Repartidor";
        }

        //VERIFICAR RESTAURANTE
        Optional<Restaurante> restaurante = restauranteDAO.findBycif(claveUnica);
        if (restaurante.isPresent() && restaurante.get().getPasswordRestaurante().equals(contra)) {
            return "Restaurante";
        }

        // Si no coincide, devuelve null o un mensaje de error
        return null;

    }

}
