package asistenciamotor360.modelo;

/**
 * Estados vigentes del personal tecnico.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public enum EstadoTecnico {

    DISPONIBLE("Disponible"),
    EN_ATENCION("En atencion de siniestro"),
    FUERA_SERVICIO("Fuera de servicio");

    private final String etiqueta;

    private EstadoTecnico(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Retorna el nombre legible del estado.
     *
     * @return etiqueta del estado.
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Retorna la etiqueta del estado para mostrarla en la interfaz.
     *
     * @return etiqueta del estado.
     */
    @Override
    public String toString() {
        return etiqueta;
    }
}
