package asistenciamotor360.modelo;

/**
 * Tipos de incidente que atiende la compania en carretera.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public enum TipoIncidente {

    AVERIA_MECANICA("Averia mecanica"),
    PINCHAZO("Pinchazo o llanta averiada"),
    BATERIA_DESCARGADA("Bateria descargada"),
    REMOLQUE("Remolque con grua"),
    CERRAJERIA("Cerrajeria vehicular"),
    COMBUSTIBLE("Entrega de combustible"),
    DIAGNOSTICO("Diagnostico preventivo");

    private final String etiqueta;

    private TipoIncidente(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Retorna el nombre legible del tipo de incidente.
     *
     * @return etiqueta del incidente.
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Retorna la etiqueta del incidente para mostrarla en la interfaz.
     *
     * @return etiqueta del incidente.
     */
    @Override
    public String toString() {
        return etiqueta;
    }
}
