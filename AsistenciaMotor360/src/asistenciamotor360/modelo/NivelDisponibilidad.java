package asistenciamotor360.modelo;

public enum NivelDisponibilidad {

    ALTA("Alta"),

    MEDIA("Media"),

    BAJA("Baja");

    private final String etiqueta;

    private NivelDisponibilidad(String etiqueta) {
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