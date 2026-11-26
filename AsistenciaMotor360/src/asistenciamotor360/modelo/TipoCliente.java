
package asistenciamotor360.modelo;

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

    public String getEtiqueta() {
        return etiqueta;
    }

    public int getPesoPrioridad() {
        return pesoPrioridad;
    }

    @Override
    public String toString() {
        return etiqueta;
    }

}
