package asistenciamotor360.modelo;

/**
 * Estados por los que atraviesa una solicitud de asistencia.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public enum EstadoSolicitud {

    PENDIENTE("Pendiente"),
    ASIGNADA("Recursos asignados"),
    ATENDIDA("Atendida"),
    SOLUCIONADA("Solucionada");

    private final String etiqueta;

    private EstadoSolicitud(String etiqueta) {
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
