package asistenciamotor360.modelo;

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

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }

}