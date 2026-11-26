
package asistenciamotor360.modelo;

public class UnidadMovil {

    private String uuid;
    private String codigo;
    private TipoUnidad tipo;
    private EstadoUnidad estado;
    private ZonaOperacion zona;
    private NivelDisponibilidad disponibilidad;

    public UnidadMovil(String uuid, String codigo, TipoUnidad tipo, EstadoUnidad estado,
            ZonaOperacion zona, NivelDisponibilidad disponibilidad) {

        this.uuid = uuid;
        this.codigo = codigo;
        this.tipo = tipo;
        this.estado = estado;
        this.zona = zona;
        this.disponibilidad = disponibilidad;
    }

    public String getUuid() {
        return uuid;
    }

    public String getCodigo() {
        return codigo;
    }

    public TipoUnidad getTipo() {
        return tipo;
    }

    public EstadoUnidad getEstado() {
        return estado;
    }

    public void setEstado(EstadoUnidad estado) {
        this.estado = estado;
    }

    public ZonaOperacion getZona() {
        return zona;
    }

    public NivelDisponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    public boolean estaDisponible() {
        return EstadoUnidad.DISPONIBLE.equals(estado);
    }

    public void asignar() {
        this.estado = EstadoUnidad.ASIGNADA;
    }

    public void liberar() {
        this.estado = EstadoUnidad.DISPONIBLE;
    }

    @Override
    public String toString() {
        return codigo + " - " + tipo.getEtiqueta();
    }

}
