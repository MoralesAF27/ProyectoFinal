package asistenciamotor360.modelo;

/**
 * Areas de especializacion del personal tecnico.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public enum Especialidad {

    MECANICA_GENERAL("Mecanica general"),
    ELECTRICIDAD("Electricidad automotriz"),
    LLANTAS("Llantas y pinchazos"),
    CERRAJERIA("Cerrajeria vehicular"),
    GRUA_REMOLQUE("Grua y remolque"),
    COMBUSTIBLE("Suministro de combustible"),
    DIAGNOSTICO("Diagnostico preventivo");

    private final String etiqueta;

    private Especialidad(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Retorna el nombre legible de la especialidad.
     *
     * @return etiqueta de la especialidad.
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Retorna la etiqueta de la especialidad para mostrarla en la interfaz.
     *
     * @return etiqueta de la especialidad.
     */
    @Override
    public String toString() {
        return etiqueta;
    }
}
