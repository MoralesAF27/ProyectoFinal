package asistenciamotor360.modelo;

import java.time.LocalDateTime;

/**
 * Representa una entrada del historial reciente de acciones realizado por los
 * operadores y el supervisor del centro de control.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class RegistroAccion {

    private LocalDateTime fecha;
    private String categoria;
    private String descripcion;

    /**
     * Crea un registro de auditoria.
     *
     * @param categoria   modulo o tipo de accion realizada.
     * @param descripcion detalle de la accion.
     */
    public RegistroAccion(String categoria, String descripcion) {
        this.fecha = LocalDateTime.now();
        this.categoria = categoria;
        this.descripcion = descripcion;
    }

    /**
     * Retorna la fecha y hora de la accion.
     *
     * @return fecha del registro.
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Retorna la categoria del registro.
     *
     * @return categoria de la accion.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Retorna la descripcion de la accion.
     *
     * @return descripcion del registro.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Retorna el registro con formato de hora y detalle.
     *
     * @return descripcion legible del registro.
     */
    @Override
    public String toString() {
        String hora = String.format("%02d:%02d:%02d", fecha.getHour(), fecha.getMinute(),
                fecha.getSecond());
        return hora + " [" + categoria + "] " + descripcion;
    }
}
