
package asistenciamotor360.modelo;

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

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }

}
