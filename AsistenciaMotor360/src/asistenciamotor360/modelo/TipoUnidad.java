package asistenciamotor360.modelo;

/**
 * Categorias de unidades moviles con las que cuenta la compania.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public enum TipoUnidad {

    GRUA("Grua de remolque"),
    MOTO_RESPUESTA("Motocicleta de respuesta rapida"),
    VEHICULO_UTILITARIO("Vehiculo utilitario"),
    COCHE_LIGERO("Coche ligero");

    private final String etiqueta;

    private TipoUnidad(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Retorna el nombre legible de la categoria.
     *
     * @return etiqueta de la categoria.
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Retorna la etiqueta de la categoria para mostrarla en la interfaz.
     *
     * @return etiqueta de la categoria.
     */
    @Override
    public String toString() {
        return etiqueta;
    }
}
