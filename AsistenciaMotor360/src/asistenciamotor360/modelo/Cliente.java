package asistenciamotor360.modelo;

public class Cliente {

    private String identificacion;
    private String nombre;
    private TipoCliente tipo;
    private String telefono;
    private String direccion;

    public Cliente(String identificacion, String nombre, TipoCliente tipo, String telefono,
            String direccion) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.tipo = tipo;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public String toString() {
        return nombre + " (" + tipo.getEtiqueta() + ")";
    }

}