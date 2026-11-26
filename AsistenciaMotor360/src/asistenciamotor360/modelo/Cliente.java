package asistenciamotor360.modelo;

/**
 * Representa a un cliente de la compania. Toda solicitud de asistencia debe
 * estar vinculada obligatoriamente a un cliente registrado.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class Cliente {

    private String identificacion;
    private String nombre;
    private TipoCliente tipo;
    private String telefono;
    private String direccion;

    /**
     * Crea un cliente con sus datos de contacto.
     *
     * @param identificacion documento o NIT del cliente.
     * @param nombre         nombre o razon social.
     * @param tipo           perfil del cliente.
     * @param telefono       telefono de contacto.
     * @param direccion      direccion principal.
     */
    public Cliente(String identificacion, String nombre, TipoCliente tipo, String telefono,
            String direccion) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.tipo = tipo;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    /**
     * Retorna la identificacion del cliente.
     *
     * @return identificacion del cliente.
     */
    public String getIdentificacion() {
        return identificacion;
    }

    /**
     * Retorna el nombre o razon social del cliente.
     *
     * @return nombre del cliente.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Retorna el perfil del cliente.
     *
     * @return tipo de cliente.
     */
    public TipoCliente getTipo() {
        return tipo;
    }

    /**
     * Retorna el telefono de contacto.
     *
     * @return telefono del cliente.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Retorna la direccion principal del cliente.
     *
     * @return direccion del cliente.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Retorna una descripcion corta del cliente para listados e interfaces.
     *
     * @return nombre y perfil del cliente.
     */
    @Override
    public String toString() {
        return nombre + " (" + tipo.getEtiqueta() + ")";
    }
}
