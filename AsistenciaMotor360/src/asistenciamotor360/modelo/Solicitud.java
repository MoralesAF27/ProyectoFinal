
package asistenciamotor360.modelo;

import java.time.LocalDateTime;

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

    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public TipoIncidente getTipoIncidente() {
        return tipoIncidente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public ZonaOperacion getZona() {
        return zona;
    }

    public int getNivelRiesgo() {
        return nivelRiesgo;
    }

    public int getAfectacionVial() {
        return afectacionVial;
    }

    public int getSeveridad() {
        return severidad;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public LocalDateTime getFechaAtencion() {
        return fechaAtencion;
    }

    public LocalDateTime getFechaSolucion() {
        return fechaSolucion;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public boolean isPrioritaria() {
        return prioritaria;
    }

    public void setPrioritaria(boolean prioritaria) {
        this.prioritaria = prioritaria;
    }

    public int getPuntajePrioridad() {
        return puntajePrioridad;
    }

    public void setPuntajePrioridad(int puntajePrioridad) {
        this.puntajePrioridad = puntajePrioridad;
    }

    public String getMotivoPrioridad() {
        return motivoPrioridad;
    }

    public void setMotivoPrioridad(String motivoPrioridad) {
        this.motivoPrioridad = motivoPrioridad;
    }

    public UnidadMovil getUnidad() {
        return unidad;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public boolean tieneRecursosAsignados() {
        return unidad != null && tecnico != null;
    }

    public void asignar(UnidadMovil unidad, Tecnico tecnico) {
        this.unidad = unidad;
        this.tecnico = tecnico;
        this.fechaAsignacion = LocalDateTime.now();
        this.estado = EstadoSolicitud.ASIGNADA;
    }

    public void marcarAtendida() {
        if (!tieneRecursosAsignados()) {
            throw new ReglaNegocioExcepcion(
                    "La solicitud " + id + " no puede marcarse atendida sin recursos asignados.");
        }
        this.estado = EstadoSolicitud.ATENDIDA;
        this.fechaAtencion = LocalDateTime.now();
    }

    public void marcarSolucionada() {
        this.estado = EstadoSolicitud.SOLUCIONADA;
        this.fechaSolucion = LocalDateTime.now();
    }

    public void liberarRecursos() {
        this.unidad = null;
        this.tecnico = null;
    }

    public void revertirAsignacion() {
        this.estado = EstadoSolicitud.PENDIENTE;
        this.unidad = null;
        this.tecnico = null;
        this.fechaAsignacion = null;
        this.fechaAtencion = null;
    }

    public void revertirAtencion(EstadoSolicitud estado, LocalDateTime fechaAtencion) {
        this.estado = estado;
        this.fechaAtencion = fechaAtencion;
    }

    public void revertirSolucion(EstadoSolicitud estado, LocalDateTime fechaAtencion) {
        this.estado = estado;
        this.fechaAtencion = fechaAtencion;
        this.fechaSolucion = null;
    }

    @Override
    public String toString() {
        return id + " | " + cliente.getNombre() + " | " + tipoIncidente.getEtiqueta();
    }

}
