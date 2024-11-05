package es.uclm.repartodomicilio.business.entity;

import es.uclm.repartodomicilio.business.persistence.ClienteDAO;
import es.uclm.repartodomicilio.business.persistence.RepartidorDAO;
import es.uclm.repartodomicilio.business.persistence.RestauranteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Login {
    @Autowired
    private ClienteDAO clienteDAO;

    @Autowired
    private RestauranteDAO restauranteDAO;

    @Autowired
    private RepartidorDAO repartidorDAO;

    public String autenticarUser(String claveUnica, String contra){
        // VERIFICAR CLIENTE
        Optional<Cliente> cliente = clienteDAO.findByDni(claveUnica); // claveUnica debería ser el DNI
        if (cliente.isPresent() && cliente.get().getPassword().equals(contra)){
            return "Cliente";
        }

        // VERIFICAR REPARTIDOR
        Optional<Repartidor> repartidor = repartidorDAO.findByDniRepartidor(claveUnica);
        if (repartidor.isPresent() && repartidor.get().getPassword_repartidor().equals(contra)) {
            return "Repartidor";
        }

        //VERIFICAR RESTAURANTE
        Optional<Restaurante> restaurante = restauranteDAO.findBycif(claveUnica);
        if (restaurante.isPresent() && restaurante.get().getPassword_restaurante().equals(contra)) {
            return "Restaurante";
        }

        // Si no coincide, devuelve null o un mensaje de error
        return null;

    }

}
