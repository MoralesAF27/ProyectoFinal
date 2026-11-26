package asistenciamotor360.modelo.estructuras;

public class TablaHash<K, V> {

    private static final int CAPACIDAD_INICIAL = 31;
    private static final double FACTOR_CARGA = 0.75;

    private NodoHash<K, V>[] cubetas;
    private int tamano;

    @SuppressWarnings("unchecked")
    public TablaHash() {
        this.cubetas = new NodoHash[CAPACIDAD_INICIAL];
        this.tamano = 0;
    }

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

    public boolean contieneClave(K clave) {
        return obtener(clave) != null;
    }

    public int tamano() {
        return tamano;
    }

    public boolean estaVacia() {
        return tamano == 0;
    }

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