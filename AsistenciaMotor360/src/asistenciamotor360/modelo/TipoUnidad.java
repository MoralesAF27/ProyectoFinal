
package asistenciamotor360.modelo;

public enum TipoUnidad {

    GRUA("Grua de remolque"),

    MOTO_RESPUESTA("Motocicleta de respuesta rapida"),

    VEHICULO_UTILITARIO("Vehiculo utilitario"),

    COCHE_LIGERO("Coche ligero");

    private final String etiqueta;

    private TipoUnidad(String etiqueta) {
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
