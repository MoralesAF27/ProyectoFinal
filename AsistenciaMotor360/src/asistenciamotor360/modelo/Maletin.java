package asistenciamotor360.modelo;

public class Maletin {

    private String codigo;
    private String contenido;
    private EstadoMaletin estado;

    public Maletin(String codigo, String contenido, EstadoMaletin estado) {
        this.codigo = codigo;
        this.contenido = contenido;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getContenido() {
        return contenido;
    }

    public EstadoMaletin getEstado() {
        return estado;
    }

    public void setEstado(EstadoMaletin estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return codigo + " | " + contenido + " | " + estado.getEtiqueta();
    }

}