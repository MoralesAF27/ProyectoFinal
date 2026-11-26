package asistenciamotor360.modelo;

import java.time.LocalDateTime;

/**
 * Representa una peticion de asistencia recibida por la mesa de ayuda. Almacena
 * al cliente vinculado, la clasificacion del incidente, los factores de riesgo
 * evaluados, el puntaje de prioridad y los recursos asignados.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class Solicitud {

    private String id;
    private Cliente cliente;
    private TipoIncidente tipoIncidente;
    private String descripcion;
    private ZonaOperacion zona;
    private int nivelRiesgo;
    private int afectacionVial;
    private int severidad;
    private LocalDateTime fechaRecepcion;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaAtencion;
    private LocalDateTime fechaSolucion;
    private EstadoSolicitud estado;
    private boolean prioritaria;
    private int puntajePrioridad;
    private String motivoPrioridad;
    private UnidadMovil unidad;
    private Tecnico tecnico;

    /**
     * Crea una solicitud en estado pendiente y con la fecha de recepcion actual.
     *
     * @param id             identificador de la solicitud.
     * @param cliente        cliente que reporta el incidente.
     * @param tipoIncidente  clase de averia o emergencia.
     * @param descripcion    detalle del incidente.
     * @param zona           zona donde ocurre el incidente.
     * @param nivelRiesgo    nivel de peligro entre uno y cinco.
     * @param afectacionVial congestion vehicular generada entre uno y cinco.
     * @param severidad      gravedad de la averia entre uno y cinco.
     */
    public Solicitud(String id, Cliente cliente, TipoIncidente tipoIncidente, String descripcion,
            ZonaOperacion zona, int nivelRiesgo, int afectacionVial, int severidad) {
        this.id = id;
        this.cliente = cliente;
        this.tipoIncidente = tipoIncidente;
        this.descripcion = descripcion;
        this.zona = zona;
        this.nivelRiesgo = nivelRiesgo;
        this.afectacionVial = afectacionVial;
        this.severidad = severidad;
        this.fechaRecepcion = LocalDateTime.now();
        this.estado = EstadoSolicitud.PENDIENTE;
        this.prioritaria = false;
        this.puntajePrioridad = 0;
        this.motivoPrioridad = "Sin evaluar";
    }

    /**
     * Retorna el identificador de la solicitud.
     *
     * @return identificador de la solicitud.
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna el cliente vinculado a la solicitud.
     *
     * @return cliente de la solicitud.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Retorna el tipo de incidente reportado.
     *
     * @return tipo de incidente.
     */
    public TipoIncidente getTipoIncidente() {
        return tipoIncidente;
    }

    /**
     * Retorna la descripcion del incidente.
     *
     * @return descripcion registrada.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Retorna la zona del incidente.
     *
     * @return zona de atencion.
     */
    public ZonaOperacion getZona() {
        return zona;
    }

    /**
     * Retorna el nivel de riesgo evaluado.
     *
     * @return nivel de riesgo entre uno y cinco.
     */
    public int getNivelRiesgo() {
        return nivelRiesgo;
    }

    /**
     * Retorna la afectacion vial generada.
     *
     * @return afectacion vial entre uno y cinco.
     */
    public int getAfectacionVial() {
        return afectacionVial;
    }

    /**
     * Retorna la severidad de la averia.
     *
     * @return severidad entre uno y cinco.
     */
    public int getSeveridad() {
        return severidad;
    }

    /**
     * Retorna la fecha y hora de recepcion de la solicitud.
     *
     * @return fecha de recepcion.
     */
    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    /**
     * Retorna la fecha y hora de asignacion de recursos.
     *
     * @return fecha de asignacion o nula si no se ha asignado.
     */
    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    /**
     * Retorna la fecha y hora en que la solicitud fue marcada como atendida.
     *
     * @return fecha de atencion o nula si no ha sido atendida.
     */
    public LocalDateTime getFechaAtencion() {
        return fechaAtencion;
    }

    /**
     * Retorna la fecha y hora de solucion del servicio.
     *
     * @return fecha de solucion o nula si no se ha solucionado.
     */
    public LocalDateTime getFechaSolucion() {
        return fechaSolucion;
    }

    /**
     * Retorna el estado actual de la solicitud.
     *
     * @return estado de la solicitud.
     */
    public EstadoSolicitud getEstado() {
        return estado;
    }

    /**
     * Actualiza el estado de la solicitud.
     *
     * @param estado nuevo estado.
     */
    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    /**
     * Indica si la solicitud fue escalada como critica o prioritaria.
     *
     * @return verdadero si la solicitud es prioritaria.
     */
    public boolean isPrioritaria() {
        return prioritaria;
    }

    /**
     * Define si la solicitud se atiende por la cola prioritaria.
     *
     * @param prioritaria nuevo valor de prioridad.
     */
    public void setPrioritaria(boolean prioritaria) {
        this.prioritaria = prioritaria;
    }

    /**
     * Retorna el puntaje de prioridad calculado.
     *
     * @return puntaje entre cero y cincuenta.
     */
    public int getPuntajePrioridad() {
        return puntajePrioridad;
    }

    /**
     * Actualiza el puntaje de prioridad de la solicitud.
     *
     * @param puntajePrioridad nuevo puntaje.
     */
    public void setPuntajePrioridad(int puntajePrioridad) {
        this.puntajePrioridad = puntajePrioridad;
    }

    /**
     * Retorna la justificacion de la clasificacion de prioridad.
     *
     * @return motivo de prioridad.
     */
    public String getMotivoPrioridad() {
        return motivoPrioridad;
    }

    /**
     * Actualiza la justificacion de la clasificacion de prioridad.
     *
     * @param motivoPrioridad nuevo motivo.
     */
    public void setMotivoPrioridad(String motivoPrioridad) {
        this.motivoPrioridad = motivoPrioridad;
    }

    /**
     * Retorna la unidad movil asignada.
     *
     * @return unidad asignada o nula si aun no se ha despachado.
     */
    public UnidadMovil getUnidad() {
        return unidad;
    }

    /**
     * Retorna el tecnico asignado.
     *
     * @return tecnico asignado o nulo si aun no se ha despachado.
     */
    public Tecnico getTecnico() {
        return tecnico;
    }

    /**
     * Indica si la solicitud cuenta con recursos asignados.
     *
     * @return verdadero si tiene unidad y tecnico.
     */
    public boolean tieneRecursosAsignados() {
        return unidad != null && tecnico != null;
    }

    /**
     * Asigna los recursos que atenderan el incidente y cambia el estado a
     * asignada.
     *
     * @param unidad  unidad movil despachada.
     * @param tecnico tecnico responsable del servicio.
     */
    public void asignar(UnidadMovil unidad, Tecnico tecnico) {
        this.unidad = unidad;
        this.tecnico = tecnico;
        this.fechaAsignacion = LocalDateTime.now();
        this.estado = EstadoSolicitud.ASIGNADA;
    }

    /**
     * Marca la solicitud como atendida exigiendo recursos previamente asignados.
     *
     * @throws ReglaNegocioExcepcion si la solicitud no tiene recursos asignados.
     */
    public void marcarAtendida() {
        if (!tieneRecursosAsignados()) {
            throw new ReglaNegocioExcepcion(
                    "La solicitud " + id + " no puede marcarse atendida sin recursos asignados.");
        }
        this.estado = EstadoSolicitud.ATENDIDA;
        this.fechaAtencion = LocalDateTime.now();
    }

    /**
     * Marca la solicitud como solucionada y registra la fecha de cierre.
     */
    public void marcarSolucionada() {
        this.estado = EstadoSolicitud.SOLUCIONADA;
        this.fechaSolucion = LocalDateTime.now();
    }

    /**
     * Retira los recursos asignados de la solicitud.
     */
    public void liberarRecursos() {
        this.unidad = null;
        this.tecnico = null;
    }

    /**
     * Restaura la solicitud al estado pendiente, sin recursos ni fecha de
     * asignacion. Se emplea al deshacer un despacho.
     */
    public void revertirAsignacion() {
        this.estado = EstadoSolicitud.PENDIENTE;
        this.unidad = null;
        this.tecnico = null;
        this.fechaAsignacion = null;
        this.fechaAtencion = null;
    }

    /**
     * Restaura la solicitud al estado previo a la marcacion de atencion.
     *
     * @param estado        estado que tenia la solicitud antes de la atencion.
     * @param fechaAtencion fecha de atencion anterior, puede ser nula.
     */
    public void revertirAtencion(EstadoSolicitud estado, LocalDateTime fechaAtencion) {
        this.estado = estado;
        this.fechaAtencion = fechaAtencion;
    }

    /**
     * Restaura la solicitud al estado previo a la solucion del servicio.
     *
     * @param estado        estado que tenia la solicitud antes de solucionarse.
     * @param fechaAtencion fecha de atencion anterior, puede ser nula.
     */
    public void revertirSolucion(EstadoSolicitud estado, LocalDateTime fechaAtencion) {
        this.estado = estado;
        this.fechaAtencion = fechaAtencion;
        this.fechaSolucion = null;
    }

    /**
     * Retorna una descripcion corta de la solicitud para listados e interfaces.
     *
     * @return identificador, cliente y tipo de incidente.
     */
    @Override
    public String toString() {
        return id + " | " + cliente.getNombre() + " | " + tipoIncidente.getEtiqueta();
    }
}
