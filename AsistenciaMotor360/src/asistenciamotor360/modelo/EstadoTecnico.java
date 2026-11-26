package asistenciamotor360.modelo;

public enum EstadoTecnico {

    DISPONIBLE("Disponible"),

    EN_ATENCION("En atencion de siniestro"),

    FUERA_SERVICIO("Fuera de servicio");

    private final String etiqueta;

    private EstadoTecnico(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }

}