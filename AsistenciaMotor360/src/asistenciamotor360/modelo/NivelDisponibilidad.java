package asistenciamotor360.modelo;

/**
 * Niveles de disponibilidad con los que se clasifica una unidad movil.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public enum NivelDisponibilidad {

    ALTA("Alta"),
    MEDIA("Media"),
    BAJA("Baja");

    private final String etiqueta;

    private NivelDisponibilidad(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Retorna el nombre legible del nivel.
     *
     * @return etiqueta del nivel.
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Retorna la etiqueta del nivel para mostrarla en la interfaz.
     *
     * @return etiqueta del nivel.
     */
    @Override
    public String toString() {
        return etiqueta;
    }
}
