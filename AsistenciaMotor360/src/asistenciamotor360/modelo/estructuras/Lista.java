package asistenciamotor360.modelo.estructuras;

/**
 * Implementacion propia de una lista dinamica basada en arreglo redimensionable.
 * Sustituye a las listas del framework de colecciones de Java, que no esta
 * permitido utilizar en el proyecto.
 *
 * @param <T> tipo de elemento almacenado.
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class Lista<T> {

    private static final int CAPACIDAD_INICIAL = 10;

    private Object[] elementos;
    private int tamano;

    /**
     * Crea una lista vacia con capacidad inicial de diez posiciones.
     */
    public Lista() {
        this.elementos = new Object[CAPACIDAD_INICIAL];
        this.tamano = 0;
    }

    /**
     * Agrega un elemento al final de la lista.
     *
     * @param elemento elemento que se desea almacenar.
     */
    public void agregar(T elemento) {
        asegurarCapacidad(tamano + 1);
        elementos[tamano] = elemento;
        tamano++;
    }

    /**
     * Inserta un elemento en la posicion indicada, desplazando los elementos
     * posteriores una posicion hacia la derecha.
     *
     * @param indice   posicion de insercion, entre cero y el tamano actual.
     * @param elemento elemento que se desea almacenar.
     * @throws IndexOutOfBoundsException si el indice no es valido.
     */
    public void agregar(int indice, T elemento) {
        if (indice < 0 || indice > tamano) {
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + indice);
        }
        asegurarCapacidad(tamano + 1);
        for (int i = tamano; i > indice; i--) {
            elementos[i] = elementos[i - 1];
        }
        elementos[indice] = elemento;
        tamano++;
    }

    /**
     * Obtiene el elemento almacenado en la posicion indicada.
     *
     * @param indice posicion del elemento.
     * @return el elemento solicitado.
     * @throws IndexOutOfBoundsException si el indice no es valido.
     */
    @SuppressWarnings("unchecked")
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + indice);
        }
        return (T) elementos[indice];
    }

    /**
     * Reemplaza el elemento de la posicion indicada.
     *
     * @param indice   posicion a modificar.
     * @param elemento nuevo elemento.
     * @throws IndexOutOfBoundsException si el indice no es valido.
     */
    public void reemplazar(int indice, T elemento) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + indice);
        }
        elementos[indice] = elemento;
    }

    /**
     * Elimina el elemento de la posicion indicada y retorna su valor.
     *
     * @param indice posicion del elemento a eliminar.
     * @return el elemento eliminado.
     * @throws IndexOutOfBoundsException si el indice no es valido.
     */
    @SuppressWarnings("unchecked")
    public T eliminar(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + indice);
        }
        T eliminado = (T) elementos[indice];
        for (int i = indice; i < tamano - 1; i++) {
            elementos[i] = elementos[i + 1];
        }
        elementos[tamano - 1] = null;
        tamano--;
        return eliminado;
    }

    /**
     * Elimina la primera ocurrencia del elemento indicado.
     *
     * @param elemento elemento que se desea eliminar.
     * @return verdadero si el elemento fue encontrado y eliminado.
     */
    public boolean eliminar(T elemento) {
        int indice = indiceDe(elemento);
        if (indice >= 0) {
            eliminar(indice);
            return true;
        }
        return false;
    }

    /**
     * Busca la primera ocurrencia de un elemento dentro de la lista.
     *
     * @param elemento elemento buscado.
     * @return la posicion del elemento o menos uno si no se encuentra.
     */
    public int indiceDe(T elemento) {
        for (int i = 0; i < tamano; i++) {
            if (sonIguales(elementos[i], elemento)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Verifica si un elemento se encuentra almacenado en la lista.
     *
     * @param elemento elemento buscado.
     * @return verdadero si el elemento pertenece a la lista.
     */
    public boolean contiene(T elemento) {
        return indiceDe(elemento) >= 0;
    }

    /**
     * Retorna la cantidad de elementos almacenados.
     *
     * @return tamano actual de la lista.
     */
    public int tamano() {
        return tamano;
    }

    /**
     * Indica si la lista no tiene elementos.
     *
     * @return verdadero si la lista esta vacia.
     */
    public boolean estaVacia() {
        return tamano == 0;
    }

    /**
     * Elimina todos los elementos de la lista.
     */
    public void limpiar() {
        for (int i = 0; i < tamano; i++) {
            elementos[i] = null;
        }
        tamano = 0;
    }

    /**
     * Reemplaza el contenido de la lista con los elementos indicados.
     *
     * @param elementosnuevos elementos que conformaran la lista.
     */
    public void cargar(Lista<T> elementosnuevos) {
        limpiar();
        asegurarCapacidad(elementosnuevos.tamano());
        for (int i = 0; i < elementosnuevos.tamano(); i++) {
            agregar(elementosnuevos.obtener(i));
        }
    }

    /**
     * Crea una copia independiente de la lista.
     *
     * @return nueva lista con los mismos elementos en el mismo orden.
     */
    public Lista<T> copia() {
        Lista<T> copia = new Lista<T>();
        for (int i = 0; i < tamano; i++) {
            copia.agregar(obtener(i));
        }
        return copia;
    }

    /**
     * Ordena los elementos de la lista con el criterio indicado, empleando el
     * algoritmo de insercion.
     *
     * @param comparador criterio de ordenamiento.
     */
    public void ordenar(Comparador<T> comparador) {
        for (int i = 1; i < tamano; i++) {
            T actual = obtener(i);
            int j = i - 1;
            while (j >= 0 && comparador.comparar(obtener(j), actual) > 0) {
                reemplazar(j + 1, obtener(j));
                j--;
            }
            reemplazar(j + 1, actual);
        }
    }

    private void asegurarCapacidad(int capacidadRequerida) {
        if (capacidadRequerida > elementos.length) {
            int nuevaCapacidad = elementos.length * 2;
            if (nuevaCapacidad < capacidadRequerida) {
                nuevaCapacidad = capacidadRequerida;
            }
            Object[] nuevoArreglo = new Object[nuevaCapacidad];
            for (int i = 0; i < tamano; i++) {
                nuevoArreglo[i] = elementos[i];
            }
            elementos = nuevoArreglo;
        }
    }

    private boolean sonIguales(Object primero, Object segundo) {
        if (primero == null) {
            return segundo == null;
        }
        return primero.equals(segundo);
    }
}
