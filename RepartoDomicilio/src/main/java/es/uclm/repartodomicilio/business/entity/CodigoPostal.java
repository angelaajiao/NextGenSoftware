package es.uclm.repartodomicilio.business.entity;

public enum CodigoPostal {
    TALAVERA(45600),
    MADRID(28000);

    private final int codigo;

    // Constructor
    CodigoPostal(int codigo) {
        this.codigo = codigo;
    }

    // Método para obtener el valor numérico
    public int getCodigo() {
        return codigo;
    }
}

