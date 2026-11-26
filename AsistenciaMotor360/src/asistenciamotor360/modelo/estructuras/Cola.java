package asistenciamotor360.modelo.estructuras;

/**
 * Implementacion propia de una cola simple (estructura FIFO) basada en arreglo
 * circular. Garantiza que las solicitudes ordinarias se atiendan estrictamente
 * en el orden de llegada.
 *
 * @param <T> tipo de elemento almacenado.
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class Cola<T> {

    private static final int CAPACIDAD_INICIAL = 10;

    private Object[] elementos;
    private int frente;
    private int fin;
    private int tamano;

    /**
     * Crea una cola vacia.
     */
    public Cola() {
        this.elementos = new Object[CAPACIDAD_INICIAL];
        this.frente = 0;
        this.fin = 0;
        this.tamano = 0;
    }

    /**
     * Ubica un elemento al final de la cola.
     *
     * @param elemento elemento que se desea encolar.
     */
    public void encolar(T elemento) {
        if (tamano == elementos.length) {
            expandir();
        }
        elementos[fin] = elemento;
        fin = (fin + 1) % elementos.length;
        tamano++;
    }

    /**
     * Retira y retorna el elemento ubicado al frente de la cola.
     *
     * @return elemento del frente.
     * @throws EstructuraVaciaExcepcion si la cola no tiene elementos.
     */
    @SuppressWarnings("unchecked")
    public T desencolar() {
        if (estaVacia()) {
            throw new EstructuraVaciaExcepcion("La cola no tiene elementos.");
        }
        T elemento = (T) elementos[frente];
        elementos[frente] = null;
        frente = (frente + 1) % elementos.length;
        tamano--;
        return elemento;
    }

    /**
     * Consulta el elemento del frente sin retirarlo.
     *
     * @return elemento del frente.
     * @throws EstructuraVaciaExcepcion si la cola no tiene elementos.
     */
    @SuppressWarnings("unchecked")
    public T frente() {
        if (estaVacia()) {
            throw new EstructuraVaciaExcepcion("La cola no tiene elementos.");
        }
        return (T) elementos[frente];
    }

    /**
     * Retorna la cantidad de elementos encolados.
     *
     * @return tamano de la cola.
     */
    public int tamano() {
        return tamano;
    }

    /**
     * Indica si la cola no tiene elementos.
     *
     * @return verdadero si la cola esta vacia.
     */
    public boolean estaVacia() {
        return tamano == 0;
    }

    /**
     * Verifica si un elemento se encuentra en la cola.
     *
     * @param elemento elemento buscado.
     * @return verdadero si el elemento pertenece a la cola.
     */
    public boolean contiene(T elemento) {
        for (int i = 0; i < tamano; i++) {
            Object actual = elementos[(frente + i) % elementos.length];
            if (actual != null && actual.equals(elemento)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina la primera ocurrencia de un elemento conservando el orden del
     * resto de la cola.
     *
     * @param elemento elemento que se desea eliminar.
     * @return verdadero si el elemento fue encontrado y eliminado.
     */
    public boolean eliminar(T elemento) {
        Lista<T> restantes = new Lista<T>();
        boolean eliminado = false;
        for (int i = 0; i < tamano; i++) {
            @SuppressWarnings("unchecked")
            T actual = (T) elementos[(frente + i) % elementos.length];
            if (!eliminado && actual != null && actual.equals(elemento)) {
                eliminado = true;
            } else {
                restantes.agregar(actual);
            }
        }
        if (eliminado) {
            cargar(restantes);
        }
        return eliminado;
    }

    /**
     * Retorna los elementos de la cola en orden de atencion, desde el frente
     * hasta el final.
     *
     * @return lista con los elementos en orden FIFO.
     */
    @SuppressWarnings("unchecked")
    public Lista<T> listar() {
        Lista<T> lista = new Lista<T>();
        for (int i = 0; i < tamano; i++) {
            lista.agregar((T) elementos[(frente + i) % elementos.length]);
        }
        return lista;
    }

    /**
     * Reemplaza el contenido de la cola con los elementos indicados, respetando
     * el orden de la lista recibida.
     *
     * @param elementosnuevos elementos que conformaran la cola.
     */
    public void cargar(Lista<T> elementosnuevos) {
        int capacidadNecesaria = Math.max(CAPACIDAD_INICIAL, elementosnuevos.tamano() + 1);
        this.elementos = new Object[capacidadNecesaria];
        this.frente = 0;
        this.fin = 0;
        this.tamano = 0;
        for (int i = 0; i < elementosnuevos.tamano(); i++) {
            encolar(elementosnuevos.obtener(i));
        }
    }

    /**
     * Vacía la cola por completo.
     */
    public void limpiar() {
        this.elementos = new Object[CAPACIDAD_INICIAL];
        this.frente = 0;
        this.fin = 0;
        this.tamano = 0;
    }

    private void expandir() {
        Lista<T> actuales = listar();
        Object[] nuevoArreglo = new Object[elementos.length * 2];
        for (int i = 0; i < actuales.tamano(); i++) {
            nuevoArreglo[i] = actuales.obtener(i);
        }
        this.elementos = nuevoArreglo;
        this.frente = 0;
        this.fin = tamano;
    }
}
