package asistenciamotor360.modelo.estructuras;

/**
 * Implementacion propia de una pila (estructura LIFO) basada en arreglo. Se usa
 * para el apilamiento de maletines y repuestos, y para el historial de
 * transacciones que el supervisor puede deshacer.
 *
 * @param <T> tipo de elemento almacenado.
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class Pila<T> {

    private static final int CAPACIDAD_INICIAL = 10;

    private Object[] elementos;
    private int tamano;

    /**
     * Crea una pila vacia.
     */
    public Pila() {
        this.elementos = new Object[CAPACIDAD_INICIAL];
        this.tamano = 0;
    }

    /**
     * Apila un elemento en la cima.
     *
     * @param elemento elemento que se desea apilar.
     */
    public void empujar(T elemento) {
        if (tamano == elementos.length) {
            Object[] nuevoArreglo = new Object[elementos.length * 2];
            for (int i = 0; i < tamano; i++) {
                nuevoArreglo[i] = elementos[i];
            }
            elementos = nuevoArreglo;
        }
        elementos[tamano] = elemento;
        tamano++;
    }

    /**
     * Retira y retorna el elemento ubicado en la cima.
     *
     * @return elemento de la cima.
     * @throws EstructuraVaciaExcepcion si la pila no tiene elementos.
     */
    @SuppressWarnings("unchecked")
    public T desempujar() {
        if (estaVacia()) {
            throw new EstructuraVaciaExcepcion("La pila no tiene elementos.");
        }
        T elemento = (T) elementos[tamano - 1];
        elementos[tamano - 1] = null;
        tamano--;
        return elemento;
    }

    /**
     * Consulta el elemento de la cima sin retirarlo.
     *
     * @return elemento de la cima.
     * @throws EstructuraVaciaExcepcion si la pila no tiene elementos.
     */
    @SuppressWarnings("unchecked")
    public T cima() {
        if (estaVacia()) {
            throw new EstructuraVaciaExcepcion("La pila no tiene elementos.");
        }
        return (T) elementos[tamano - 1];
    }

    /**
     * Retorna la cantidad de elementos apilados.
     *
     * @return tamano de la pila.
     */
    public int tamano() {
        return tamano;
    }

    /**
     * Indica si la pila no tiene elementos.
     *
     * @return verdadero si la pila esta vacia.
     */
    public boolean estaVacia() {
        return tamano == 0;
    }

    /**
     * Verifica si un elemento se encuentra en la pila.
     *
     * @param elemento elemento buscado.
     * @return verdadero si el elemento pertenece a la pila.
     */
    public boolean contiene(T elemento) {
        for (int i = 0; i < tamano; i++) {
            if (elementos[i] != null && elementos[i].equals(elemento)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna los elementos de la pila en orden de salida, es decir, desde la
     * cima hasta la base.
     *
     * @return lista con los elementos ordenados de cima a base.
     */
    @SuppressWarnings("unchecked")
    public Lista<T> listar() {
        Lista<T> lista = new Lista<T>();
        for (int i = tamano - 1; i >= 0; i--) {
            lista.agregar((T) elementos[i]);
        }
        return lista;
    }

    /**
     * Reemplaza el contenido de la pila con los elementos indicados. El primer
     * elemento de la lista queda en la base y el ultimo en la cima.
     *
     * @param elementosnuevos elementos que conformaran la pila.
     */
    public void cargar(Lista<T> elementosnuevos) {
        limpiar();
        for (int i = 0; i < elementosnuevos.tamano(); i++) {
            empujar(elementosnuevos.obtener(i));
        }
    }

    /**
     * Vacía la pila por completo.
     */
    public void limpiar() {
        for (int i = 0; i < tamano; i++) {
            elementos[i] = null;
        }
        tamano = 0;
    }
}
