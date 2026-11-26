package asistenciamotor360.modelo;

public enum EstadoMaletin {

    DISPONIBLE("Disponible en estanteria"),

    EN_SERVICIO("En servicio"),

    EN_AUDITORIA("En auditoria");

    private final String etiqueta;

    private EstadoMaletin(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }

}