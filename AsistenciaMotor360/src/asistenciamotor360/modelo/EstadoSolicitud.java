package asistenciamotor360.modelo;

public enum EstadoSolicitud {

    PENDIENTE("Pendiente"),

    ASIGNADA("Recursos asignados"),

    ATENDIDA("Atendida"),

    SOLUCIONADA("Solucionada");

    private final String etiqueta;

    private EstadoSolicitud(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }

}