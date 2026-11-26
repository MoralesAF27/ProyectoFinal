package asistenciamotor360.modelo;

public class ReglaNegocioExcepcion extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ReglaNegocioExcepcion(String mensaje) {
        super(mensaje);
    }

}