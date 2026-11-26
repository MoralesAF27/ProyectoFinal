package asistenciamotor360.modelo;

import java.time.LocalDateTime;

/**
 * Representa una transaccion reversible ejecutada por el sistema. El supervisor
 * puede deshacer las ultimas transacciones en orden inverso al registro.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class Transaccion {

    private int consecutivo;
    private String descripcion;
    private LocalDateTime fecha;
    private Runnable reversa;

    /**
     * Crea una transaccion reversible.
     *
     * @param consecutivo numero de la transaccion dentro de la jornada.
     * @param descripcion descripcion de la operacion realizada.
     * @param reversa     accion que restaura el estado anterior.
     */
    public Transaccion(int consecutivo, String descripcion, Runnable reversa) {
        this.consecutivo = consecutivo;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
        this.reversa = reversa;
    }

    /**
     * Retorna el numero consecutivo de la transaccion.
     *
     * @return consecutivo de la transaccion.
     */
    public int getConsecutivo() {
        return consecutivo;
    }

    /**
     * Retorna la descripcion de la operacion realizada.
     *
     * @return descripcion de la transaccion.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Retorna la fecha y hora de registro de la transaccion.
     *
     * @return fecha de la transaccion.
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Ejecuta la accion que restaura el estado anterior a la transaccion.
     */
    public void deshacer() {
        reversa.run();
    }

    /**
     * Retorna una descripcion corta de la transaccion para listados.
     *
     * @return consecutivo y descripcion.
     */
    @Override
    public String toString() {
        return "#" + consecutivo + " " + descripcion;
    }
}
