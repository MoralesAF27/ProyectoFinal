package asistenciamotor360.modelo;

import java.time.LocalDateTime;

public class RegistroAccion {

    private LocalDateTime fecha;
    private String categoria;
    private String descripcion;

    public RegistroAccion(String categoria, String descripcion) {
        this.fecha = LocalDateTime.now();
        this.categoria = categoria;
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        String hora = String.format("%02d:%02d:%02d", fecha.getHour(), fecha.getMinute(),
                fecha.getSecond());

        return hora + " [" + categoria + "] " + descripcion;
    }

}