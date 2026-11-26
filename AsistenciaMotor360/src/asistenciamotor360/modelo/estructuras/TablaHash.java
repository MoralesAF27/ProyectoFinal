package asistenciamotor360.modelo.estructuras;

/**
 * Implementacion propia de una tabla hash con encadenamiento separado. Permite
 * localizar unidades, tecnicos, clientes, solicitudes e insumos por su
 * identificador sin emplear las colecciones de Java.
 *
 * @param <K> tipo de la clave.
 * @param <V> tipo del valor almacenado.
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class TablaHash<K, V> {

    private static final int CAPACIDAD_INICIAL = 31;
    private static final double FACTOR_CARGA = 0.75;

    private NodoHash<K, V>[] cubetas;
    private int tamano;

    /**
     * Crea una tabla hash vacia.
     */
    @SuppressWarnings("unchecked")
    public TablaHash() {
        this.cubetas = new NodoHash[CAPACIDAD_INICIAL];
        this.tamano = 0;
    }

    /**
     * Inserta o actualiza el valor asociado a una clave.
     *
     * @param clave clave de busqueda.
     * @param valor valor que se desea almacenar.
     */
    public void poner(K clave, V valor) {
        if (clave == null) {
            throw new IllegalArgumentException("La clave no puede ser nula.");
        }
        int indice = indiceDe(clave);
        NodoHash<K, V> actual = cubetas[indice];
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                actual.valor = valor;
                return;
            }
            actual = actual.siguiente;
        }
        NodoHash<K, V> nuevo = new NodoHash<K, V>(clave, valor);
        nuevo.siguiente = cubetas[indice];
        cubetas[indice] = nuevo;
        tamano++;
        if ((double) tamano / cubetas.length > FACTOR_CARGA) {
            reubicar();
        }
    }

    /**
     * Obtiene el valor asociado a una clave.
     *
     * @param clave clave de busqueda.
     * @return el valor asociado o nulo si la clave no existe.
     */
    public V obtener(K clave) {
        if (clave == null) {
            return null;
        }
        NodoHash<K, V> actual = cubetas[indiceDe(clave)];
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                return actual.valor;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    /**
     * Elimina la entrada asociada a una clave.
     *
     * @param clave clave de la entrada que se desea eliminar.
     * @return el valor eliminado o nulo si la clave no existia.
     */
    public V eliminar(K clave) {
        if (clave == null) {
            return null;
        }
        int indice = indiceDe(clave);
        NodoHash<K, V> actual = cubetas[indice];
        NodoHash<K, V> anterior = null;
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                if (anterior == null) {
                    cubetas[indice] = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                tamano--;
                return actual.valor;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return null;
    }

    /**
     * Verifica si una clave se encuentra registrada.
     *
     * @param clave clave de busqueda.
     * @return verdadero si la clave existe en la tabla.
     */
    public boolean contieneClave(K clave) {
        return obtener(clave) != null;
    }

    /**
     * Retorna la cantidad de entradas almacenadas.
     *
     * @return tamano de la tabla.
     */
    public int tamano() {
        return tamano;
    }

    /**
     * Indica si la tabla no tiene entradas.
     *
     * @return verdadero si la tabla esta vacia.
     */
    public boolean estaVacia() {
        return tamano == 0;
    }

    /**
     * Retorna todas las claves registradas.
     *
     * @return lista de claves.
     */
    public Lista<K> claves() {
        Lista<K> claves = new Lista<K>();
        for (int i = 0; i < cubetas.length; i++) {
            NodoHash<K, V> actual = cubetas[i];
            while (actual != null) {
                claves.agregar(actual.clave);
                actual = actual.siguiente;
            }
        }
        return claves;
    }

    /**
     * Retorna todos los valores registrados.
     *
     * @return lista de valores.
     */
    public Lista<V> valores() {
        Lista<V> valores = new Lista<V>();
        for (int i = 0; i < cubetas.length; i++) {
            NodoHash<K, V> actual = cubetas[i];
            while (actual != null) {
                valores.agregar(actual.valor);
                actual = actual.siguiente;
            }
        }
        return valores;
    }

    private int indiceDe(K clave) {
        int codigo = clave.hashCode() & 0x7FFFFFFF;
        return codigo % cubetas.length;
    }

    @SuppressWarnings("unchecked")
    private void reubicar() {
        Lista<K> claves = claves();
        Lista<V> valores = valores();
        this.cubetas = new NodoHash[cubetas.length * 2 + 1];
        this.tamano = 0;
        for (int i = 0; i < claves.tamano(); i++) {
            poner(claves.obtener(i), valores.obtener(i));
        }
    }

    /**
     * Nodo interno que conforma la cadena de cada cubeta.
     *
     * @param <K> tipo de la clave.
     * @param <V> tipo del valor.
     */
    private static class NodoHash<K, V> {

        private K clave;
        private V valor;
        private NodoHash<K, V> siguiente;

        private NodoHash(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
        }
    }
}
