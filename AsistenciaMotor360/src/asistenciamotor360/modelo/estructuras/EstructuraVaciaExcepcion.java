package asistenciamotor360.modelo.estructuras;

/**
 * Excepcion no verificada que se lanza cuando se intenta consultar o retirar un
 * elemento de una estructura de datos que no contiene elementos.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class EstructuraVaciaExcepcion extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Construye la excepcion con el mensaje indicado.
     *
     * @param mensaje descripcion del error producido.
     */
    public EstructuraVaciaExcepcion(String mensaje) {
        super(mensaje);
    }
}
