package asistenciamotor360.modelo;

/**
 * Estados del ciclo de vida de un maletin de respuesta rapida.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public enum EstadoMaletin {

    DISPONIBLE("Disponible en estanteria"),
    EN_SERVICIO("En servicio"),
    EN_AUDITORIA("En auditoria");

    private final String etiqueta;

    private EstadoMaletin(String etiqueta) {
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
