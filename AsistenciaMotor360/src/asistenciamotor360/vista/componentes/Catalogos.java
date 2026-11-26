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

public final class Catalogos {

    private Catalogos() {
    }

    public static Lista<TipoUnidad> tiposUnidad() {
        return de(TipoUnidad.values());
    }

    public static Lista<ZonaOperacion> zonas() {
        return de(ZonaOperacion.values());
    }

    public static Lista<NivelDisponibilidad> nivelesDisponibilidad() {
        return de(NivelDisponibilidad.values());
    }

    public static Lista<EstadoUnidad> estadosUnidad() {
        return de(EstadoUnidad.values());
    }

    public static Lista<Especialidad> especialidades() {
        return de(Especialidad.values());
    }

    public static Lista<EstadoTecnico> estadosTecnicos() {
        return de(EstadoTecnico.values());
    }

    public static Lista<TipoCliente> tiposCliente() {
        return de(TipoCliente.values());
    }

    public static Lista<TipoIncidente> tiposIncidente() {
        return de(TipoIncidente.values());
    }

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