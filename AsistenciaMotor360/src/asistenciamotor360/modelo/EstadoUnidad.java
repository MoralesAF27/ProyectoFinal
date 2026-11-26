package asistenciamotor360.modelo;

public enum EstadoUnidad {

    DISPONIBLE("Disponible"),

    ASIGNADA("Asignada a servicio"),

    EN_TALLER("En taller"),

    MANTENIMIENTO("En mantenimiento");

    private final String etiqueta;

    private EstadoUnidad(String etiqueta) {
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