package asistenciamotor360.modelo;

/**
 * Sectores de la ciudad y la periferia cubiertos por la operacion.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public enum ZonaOperacion {

    NORTE("Zona Norte"),
    SUR("Zona Sur"),
    CENTRO("Zona Centro"),
    ORIENTE("Zona Oriente"),
    OCCIDENTE("Zona Occidente"),
    PERIFERIA("Periferia");

    private final String etiqueta;

    private ZonaOperacion(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Retorna el nombre legible de la zona.
     *
     * @return etiqueta de la zona.
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Retorna la etiqueta de la zona para mostrarla en la interfaz.
     *
     * @return etiqueta de la zona.
     */
    @Override
    public String toString() {
        return etiqueta;
    }
}
