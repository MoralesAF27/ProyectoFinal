
package asistenciamotor360.modelo;

import java.time.LocalDateTime;

public class Transaccion {

    private int consecutivo;
    private String descripcion;
    private LocalDateTime fecha;
    private Runnable reversa;

    public Transaccion(int consecutivo, String descripcion, Runnable reversa) {
        this.consecutivo = consecutivo;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
        this.reversa = reversa;
    }

    public int getConsecutivo() {
        return consecutivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void deshacer() {
        reversa.run();
    }

    @Override
    public String toString() {
        return "#" + consecutivo + " " + descripcion;
    }

}
