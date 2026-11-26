
package asistenciamotor360.modelo;

public class Tecnico {

    private String identificacion;
    private String nombre;
    private Especialidad especialidad;
    private EstadoTecnico estado;
    private ZonaOperacion sector;
    private String idServicioActivo;

    public Tecnico(String identificacion, String nombre, Especialidad especialidad,
            EstadoTecnico estado, ZonaOperacion sector) {

        this.identificacion = identificacion;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.estado = estado;
        this.sector = sector;
        this.idServicioActivo = null;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public EstadoTecnico getEstado() {
        return estado;
    }

    public void setEstado(EstadoTecnico estado) {
        this.estado = estado;
    }

    public ZonaOperacion getSector() {
        return sector;
    }

    public String getIdServicioActivo() {
        return idServicioActivo;
    }

    public boolean estaDisponible() {
        return EstadoTecnico.DISPONIBLE.equals(estado) && idServicioActivo == null;
    }

    public void ocupar(String idServicio) {
        this.idServicioActivo = idServicio;
        this.estado = EstadoTecnico.EN_ATENCION;
    }

    public void liberar() {
        this.idServicioActivo = null;
        this.estado = EstadoTecnico.DISPONIBLE;
    }

    public void restaurar(String idServicioActivo, EstadoTecnico estado) {
        this.idServicioActivo = idServicioActivo;
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre + " - " + especialidad.getEtiqueta();
    }

}
