package asistenciamotor360.modelo;

/**
 * Representa una pieza de repuesto de alta rotacion. Las piezas se apilan y se
 * despachan bajo el principio de ultima pieza acomodada, primera despachada.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class Repuesto {

    private String codigo;
    private String nombre;

    /**
     * Crea un repuesto con su codigo y nombre.
     *
     * @param codigo identificador de la pieza.
     * @param nombre descripcion de la pieza.
     */
    public Repuesto(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    /**
     * Retorna el codigo de la pieza.
     *
     * @return codigo del repuesto.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Retorna el nombre de la pieza.
     *
     * @return nombre del repuesto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Retorna una descripcion corta del repuesto para listados e interfaces.
     *
     * @return codigo y nombre del repuesto.
     */
    @Override
    public String toString() {
        return codigo + " | " + nombre;
    }
}
