package asistenciamotor360.modelo;

/**
 * Estados operativos en los que puede encontrarse una unidad movil.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public enum EstadoUnidad {

    DISPONIBLE("Disponible"),
    ASIGNADA("Asignada a servicio"),
    EN_TALLER("En taller"),
    MANTENIMIENTO("En mantenimiento");

    private final String etiqueta;

    private EstadoUnidad(String etiqueta) {
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
