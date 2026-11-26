package asistenciamotor360.modelo;

/**
 * Representa un maletin de respuesta rapida. Los maletines se almacenan en una
 * pila y, al finalizar un servicio, pasan por una estacion de auditoria antes
 * de volver a estar disponibles.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class Maletin {

    private String codigo;
    private String contenido;
    private EstadoMaletin estado;

    /**
     * Crea un maletin con su contenido inicial.
     *
     * @param codigo    codigo unico del maletin.
     * @param contenido descripcion de los insumos que transporta.
     * @param estado    estado inicial del maletin.
     */
    public Maletin(String codigo, String contenido, EstadoMaletin estado) {
        this.codigo = codigo;
        this.contenido = contenido;
        this.estado = estado;
    }

    /**
     * Retorna el codigo del maletin.
     *
     * @return codigo del maletin.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Retorna la descripcion del contenido del maletin.
     *
     * @return contenido del maletin.
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Retorna el estado actual del maletin.
     *
     * @return estado del maletin.
     */
    public EstadoMaletin getEstado() {
        return estado;
    }

    /**
     * Actualiza el estado del maletin.
     *
     * @param estado nuevo estado.
     */
    public void setEstado(EstadoMaletin estado) {
        this.estado = estado;
    }

    /**
     * Retorna una descripcion corta del maletin para listados e interfaces.
     *
     * @return codigo y estado del maletin.
     */
    @Override
    public String toString() {
        return codigo + " | " + contenido + " | " + estado.getEtiqueta();
    }
}
