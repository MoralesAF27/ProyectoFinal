package asistenciamotor360.vista.componentes;

import asistenciamotor360.modelo.Especialidad;
import asistenciamotor360.modelo.EstadoTecnico;
import asistenciamotor360.modelo.EstadoUnidad;
import asistenciamotor360.modelo.NivelDisponibilidad;
import asistenciamotor360.modelo.TipoCliente;
import asistenciamotor360.modelo.TipoIncidente;
import asistenciamotor360.modelo.TipoUnidad;
import asistenciamotor360.modelo.ZonaOperacion;
import asistenciamotor360.modelo.estructuras.Lista;

/**
 * Fabrica de catalogos de valores constantes utilizados por los combos de la
 * interfaz grafica.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public final class Catalogos {

    private Catalogos() {
    }

    /**
     * Retorna las categorias de unidad movil.
     *
     * @return lista de categorias.
     */
    public static Lista<TipoUnidad> tiposUnidad() {
        return de(TipoUnidad.values());
    }

    /**
     * Retorna las zonas de operacion.
     *
     * @return lista de zonas.
     */
    public static Lista<ZonaOperacion> zonas() {
        return de(ZonaOperacion.values());
    }

    /**
     * Retorna los niveles de disponibilidad.
     *
     * @return lista de niveles.
     */
    public static Lista<NivelDisponibilidad> nivelesDisponibilidad() {
        return de(NivelDisponibilidad.values());
    }

    /**
     * Retorna los estados de una unidad movil.
     *
     * @return lista de estados.
     */
    public static Lista<EstadoUnidad> estadosUnidad() {
        return de(EstadoUnidad.values());
    }

    /**
     * Retorna las especialidades tecnicas.
     *
     * @return lista de especialidades.
     */
    public static Lista<Especialidad> especialidades() {
        return de(Especialidad.values());
    }

    /**
     * Retorna los estados del personal tecnico.
     *
     * @return lista de estados.
     */
    public static Lista<EstadoTecnico> estadosTecnicos() {
        return de(EstadoTecnico.values());
    }

    /**
     * Retorna los perfiles de cliente.
     *
     * @return lista de perfiles.
     */
    public static Lista<TipoCliente> tiposCliente() {
        return de(TipoCliente.values());
    }

    /**
     * Retorna los tipos de incidente.
     *
     * @return lista de incidentes.
     */
    public static Lista<TipoIncidente> tiposIncidente() {
        return de(TipoIncidente.values());
    }

    /**
     * Retorna la escala de calificacion de uno a cinco.
     *
     * @return lista con los valores de la escala.
     */
    public static Lista<Integer> escala() {
        Lista<Integer> lista = new Lista<Integer>();
        for (int i = 1; i <= 5; i++) {
            lista.agregar(Integer.valueOf(i));
        }
        return lista;
    }

    private static <T> Lista<T> de(T[] valores) {
        Lista<T> lista = new Lista<T>();
        for (int i = 0; i < valores.length; i++) {
            lista.agregar(valores[i]);
        }
        return lista;
    }
}
