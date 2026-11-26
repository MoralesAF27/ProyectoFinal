package asistenciamotor360.modelo;

/**
 * Representa una unidad movil de la flota de rescate. Cada unidad se identifica
 * de forma universal con un UUID y mantiene su categoria, estado, zona y nivel
 * de disponibilidad.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class UnidadMovil {

    private String uuid;
    private String codigo;
    private TipoUnidad tipo;
    private EstadoUnidad estado;
    private ZonaOperacion zona;
    private NivelDisponibilidad disponibilidad;

    /**
     * Crea una unidad movil con los datos obligatorios definidos por la regla
     * de negocio dos.
     *
     * @param uuid           identificador universal unico de la unidad.
     * @param codigo         placa o codigo interno de la unidad.
     * @param tipo           categoria de la unidad.
     * @param estado         estado operativo inicial.
     * @param zona           zona de operacion asignada.
     * @param disponibilidad nivel de disponibilidad de la unidad.
     */
    public UnidadMovil(String uuid, String codigo, TipoUnidad tipo, EstadoUnidad estado,
            ZonaOperacion zona, NivelDisponibilidad disponibilidad) {
        this.uuid = uuid;
        this.codigo = codigo;
        this.tipo = tipo;
        this.estado = estado;
        this.zona = zona;
        this.disponibilidad = disponibilidad;
    }

    /**
     * Retorna el identificador universal de la unidad.
     *
     * @return uuid de la unidad.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Retorna la placa o codigo interno de la unidad.
     *
     * @return codigo de la unidad.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Retorna la categoria de la unidad.
     *
     * @return tipo de unidad.
     */
    public TipoUnidad getTipo() {
        return tipo;
    }

    /**
     * Retorna el estado operativo de la unidad.
     *
     * @return estado actual.
     */
    public EstadoUnidad getEstado() {
        return estado;
    }

    /**
     * Actualiza el estado operativo de la unidad.
     *
     * @param estado nuevo estado.
     */
    public void setEstado(EstadoUnidad estado) {
        this.estado = estado;
    }

    /**
     * Retorna la zona de operacion de la unidad.
     *
     * @return zona asignada.
     */
    public ZonaOperacion getZona() {
        return zona;
    }

    /**
     * Retorna el nivel de disponibilidad de la unidad.
     *
     * @return nivel de disponibilidad.
     */
    public NivelDisponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    /**
     * Indica si la unidad puede recibir un nuevo despacho.
     *
     * @return verdadero si la unidad esta disponible.
     */
    public boolean estaDisponible() {
        return EstadoUnidad.DISPONIBLE.equals(estado);
    }

    /**
     * Marca la unidad como asignada a un servicio activo.
     */
    public void asignar() {
        this.estado = EstadoUnidad.ASIGNADA;
    }

    /**
     * Libera la unidad y la deja nuevamente disponible.
     */
    public void liberar() {
        this.estado = EstadoUnidad.DISPONIBLE;
    }

    /**
     * Retorna una descripcion corta de la unidad para listados e interfaces.
     *
     * @return codigo y categoria de la unidad.
     */
    @Override
    public String toString() {
        return codigo + " - " + tipo.getEtiqueta();
    }
}
