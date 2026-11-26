package asistenciamotor360.modelo;

/**
 * Excepcion que representa el incumplimiento de una regla de negocio de la
 * compania Asistencia Motor 360.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class ReglaNegocioExcepcion extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Construye la excepcion con el mensaje de la regla incumplida.
     *
     * @param mensaje descripcion del incumplimiento.
     */
    public ReglaNegocioExcepcion(String mensaje) {
        super(mensaje);
    }
}
