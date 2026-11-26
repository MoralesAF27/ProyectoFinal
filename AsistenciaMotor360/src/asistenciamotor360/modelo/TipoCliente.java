package asistenciamotor360.modelo;

/**
 * Perfiles de cliente atendidos por la compania. El perfil incide directamente
 * en el calculo de prioridad de una solicitud.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public enum TipoCliente {

    PARTICULAR("Particular", 1),
    FLOTA_TRANSPORTE("Flota de transporte", 2),
    ASEGURADORA("Compania de seguros", 3),
    SERVICIO_PUBLICO("Servicio publico o emergencia", 5);

    private final String etiqueta;
    private final int pesoPrioridad;

    private TipoCliente(String etiqueta, int pesoPrioridad) {
        this.etiqueta = etiqueta;
        this.pesoPrioridad = pesoPrioridad;
    }

    /**
     * Retorna el nombre legible del perfil de cliente.
     *
     * @return etiqueta del perfil.
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Retorna el peso que aporta el perfil al puntaje de prioridad.
     *
     * @return peso de prioridad del perfil.
     */
    public int getPesoPrioridad() {
        return pesoPrioridad;
    }

    /**
     * Retorna la etiqueta del perfil para mostrarla en la interfaz.
     *
     * @return etiqueta del perfil.
     */
    @Override
    public String toString() {
        return etiqueta;
    }
}
