package asistenciamotor360.modelo;

/**
 * Representa a un tecnico de la compania. Almacena su identificacion, area de
 * especialidad, estado vigente, sector de patrullaje y el servicio que tiene a
 * su cargo, de modo que nunca atienda mas de un siniestro a la vez.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class Tecnico {

    private String identificacion;
    private String nombre;
    private Especialidad especialidad;
    private EstadoTecnico estado;
    private ZonaOperacion sector;
    private String idServicioActivo;

    /**
     * Crea un tecnico con su informacion basica.
     *
     * @param identificacion documento o codigo interno del tecnico.
     * @param nombre         nombre completo.
     * @param especialidad   area de especialidad asignada.
     * @param estado         estado vigente inicial.
     * @param sector         sector de patrullaje.
     */
    public Tecnico(String identificacion, String nombre, Especialidad especialidad,
            EstadoTecnico estado, ZonaOperacion sector) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.estado = estado;
        this.sector = sector;
        this.idServicioActivo = null;
    }

    /**
     * Retorna la identificacion del tecnico.
     *
     * @return identificacion del tecnico.
     */
    public String getIdentificacion() {
        return identificacion;
    }

    /**
     * Retorna el nombre completo del tecnico.
     *
     * @return nombre del tecnico.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Retorna el area de especialidad del tecnico.
     *
     * @return especialidad asignada.
     */
    public Especialidad getEspecialidad() {
        return especialidad;
    }

    /**
     * Retorna el estado vigente del tecnico.
     *
     * @return estado actual.
     */
    public EstadoTecnico getEstado() {
        return estado;
    }

    /**
     * Actualiza el estado vigente del tecnico.
     *
     * @param estado nuevo estado.
     */
    public void setEstado(EstadoTecnico estado) {
        this.estado = estado;
    }

    /**
     * Retorna el sector de patrullaje del tecnico.
     *
     * @return sector asignado.
     */
    public ZonaOperacion getSector() {
        return sector;
    }

    /**
     * Retorna el identificador del servicio activo del tecnico.
     *
     * @return identificador del servicio o nulo si no tiene ninguno.
     */
    public String getIdServicioActivo() {
        return idServicioActivo;
    }

    /**
     * Indica si el tecnico puede recibir un nuevo servicio.
     *
     * @return verdadero si esta disponible y sin servicio activo.
     */
    public boolean estaDisponible() {
        return EstadoTecnico.DISPONIBLE.equals(estado) && idServicioActivo == null;
    }

    /**
     * Vincula al tecnico con un servicio activo.
     *
     * @param idServicio identificador del servicio asignado.
     */
    public void ocupar(String idServicio) {
        this.idServicioActivo = idServicio;
        this.estado = EstadoTecnico.EN_ATENCION;
    }

    /**
     * Libera al tecnico del servicio que tenia a cargo.
     */
    public void liberar() {
        this.idServicioActivo = null;
        this.estado = EstadoTecnico.DISPONIBLE;
    }

    /**
     * Restaura el estado y el servicio activo del tecnico. Se emplea al
     * deshacer un despacho o una solucion de servicio.
     *
     * @param idServicioActivo identificador del servicio que tenia asignado.
     * @param estado           estado que tenia el tecnico.
     */
    public void restaurar(String idServicioActivo, EstadoTecnico estado) {
        this.idServicioActivo = idServicioActivo;
        this.estado = estado;
    }

    /**
     * Retorna una descripcion corta del tecnico para listados e interfaces.
     *
     * @return nombre y especialidad del tecnico.
     */
    @Override
    public String toString() {
        return nombre + " - " + especialidad.getEtiqueta();
    }
}
