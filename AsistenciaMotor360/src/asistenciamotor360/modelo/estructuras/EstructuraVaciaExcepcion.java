package asistenciamotor360.modelo.estructuras;

public class EstructuraVaciaExcepcion extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EstructuraVaciaExcepcion(String mensaje) {
        super(mensaje);
    }

}