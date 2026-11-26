
package asistenciamotor360.modelo;

public enum TipoIncidente {

    AVERIA_MECANICA("Averia mecanica"),

    PINCHAZO("Pinchazo o llanta averiada"),

    BATERIA_DESCARGADA("Bateria descargada"),

    REMOLQUE("Remolque con grua"),

    CERRAJERIA("Cerrajeria vehicular"),

    COMBUSTIBLE("Entrega de combustible"),

    DIAGNOSTICO("Diagnostico preventivo");

    private final String etiqueta;

    private TipoIncidente(String etiqueta) {
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
