package asistenciamotor360.modelo.estructuras;

/**
 * Implementacion propia de una cola de prioridad basada en un monticulo binario
 * maximo. Se utiliza para ubicar las solicitudes criticas en los primeros
 * lugares de atencion sin recurrir a las colecciones de Java.
 *
 * @param <T> tipo de elemento almacenado.
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class ColaPrioridad<T> {

    private static final int CAPACIDAD_INICIAL = 10;

    private Object[] monticulo;
    private int tamano;
    private Comparador<T> comparador;

    /**
     * Crea una cola de prioridad vacia.
     *
     * @param comparador criterio que determina cual elemento tiene mayor
     *                   prioridad. Un resultado positivo indica que el primer
     *                   elemento se atiende antes que el segundo.
     */
    public ColaPrioridad(Comparador<T> comparador) {
        if (comparador == null) {
            throw new IllegalArgumentException("El comparador es obligatorio.");
        }
        this.comparador = comparador;
        this.monticulo = new Object[CAPACIDAD_INICIAL];
        this.tamano = 0;
    }

    /**
     * Inserta un elemento en la cola de prioridad.
     *
     * @param elemento elemento que se desea encolar.
     */
    public void encolar(T elemento) {
        if (tamano == monticulo.length) {
            Object[] nuevoArreglo = new Object[monticulo.length * 2];
            for (int i = 0; i < tamano; i++) {
                nuevoArreglo[i] = monticulo[i];
            }
            monticulo = nuevoArreglo;
        }
        monticulo[tamano] = elemento;
        tamano++;
        flotar(tamano - 1);
    }

    /**
     * Retira y retorna el elemento de mayor prioridad.
     *
     * @return elemento con mayor prioridad.
     * @throws EstructuraVaciaExcepcion si la cola no tiene elementos.
     */
    public T desencolar() {
        if (estaVacia()) {
            throw new EstructuraVaciaExcepcion("La cola de prioridad no tiene elementos.");
        }
        T mayor = obtener(0);
        monticulo[0] = monticulo[tamano - 1];
        monticulo[tamano - 1] = null;
        tamano--;
        if (tamano > 0) {
            hundir(0);
        }
        return mayor;
    }

    /**
     * Consulta el elemento de mayor prioridad sin retirarlo.
     *
     * @return elemento con mayor prioridad.
     * @throws EstructuraVaciaExcepcion si la cola no tiene elementos.
     */
    public T mirar() {
        if (estaVacia()) {
            throw new EstructuraVaciaExcepcion("La cola de prioridad no tiene elementos.");
        }
        return obtener(0);
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
            if (monticulo[i] != null && monticulo[i].equals(elemento)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina un elemento de la cola, conservando la propiedad de monticulo.
     *
     * @param elemento elemento que se desea eliminar.
     * @return verdadero si el elemento fue encontrado y eliminado.
     */
    public boolean eliminar(T elemento) {
        int indice = -1;
        for (int i = 0; i < tamano; i++) {
            if (monticulo[i] != null && monticulo[i].equals(elemento)) {
                indice = i;
                break;
            }
        }
        if (indice < 0) {
            return false;
        }
        monticulo[indice] = monticulo[tamano - 1];
        monticulo[tamano - 1] = null;
        tamano--;
        if (indice < tamano) {
            T reubicado = obtener(indice);
            int padre = (indice - 1) / 2;
            if (indice > 0 && comparador.comparar(reubicado, obtener(padre)) > 0) {
                flotar(indice);
            } else {
                hundir(indice);
            }
        }
        return true;
    }

    /**
     * Retorna los elementos en orden de atencion, del de mayor prioridad al de
     * menor prioridad, sin modificar el contenido de la cola.
     *
     * @return lista ordenada por prioridad.
     */
    public Lista<T> listar() {
        ColaPrioridad<T> copia = new ColaPrioridad<T>(comparador);
        for (int i = 0; i < tamano; i++) {
            copia.encolar(obtener(i));
        }
        Lista<T> lista = new Lista<T>();
        while (!copia.estaVacia()) {
            lista.agregar(copia.desencolar());
        }
        return lista;
    }

    /**
     * Reemplaza el contenido de la cola con los elementos indicados, sin
     * importar el orden en que lleguen.
     *
     * @param elementosnuevos elementos que conformaran la cola.
     */
    public void cargar(Lista<T> elementosnuevos) {
        this.monticulo = new Object[Math.max(CAPACIDAD_INICIAL, elementosnuevos.tamano() + 1)];
        this.tamano = 0;
        for (int i = 0; i < elementosnuevos.tamano(); i++) {
            encolar(elementosnuevos.obtener(i));
        }
    }

    /**
     * Vacía la cola por completo.
     */
    public void limpiar() {
        this.monticulo = new Object[CAPACIDAD_INICIAL];
        this.tamano = 0;
    }

    @SuppressWarnings("unchecked")
    private T obtener(int indice) {
        return (T) monticulo[indice];
    }

    private void flotar(int indice) {
        int actual = indice;
        while (actual > 0) {
            int padre = (actual - 1) / 2;
            if (comparador.comparar(obtener(actual), obtener(padre)) > 0) {
                intercambiar(actual, padre);
                actual = padre;
            } else {
                break;
            }
        }
    }

    private void hundir(int indice) {
        int actual = indice;
        while (true) {
            int izquierdo = 2 * actual + 1;
            int derecho = 2 * actual + 2;
            int mayor = actual;
            if (izquierdo < tamano && comparador.comparar(obtener(izquierdo), obtener(mayor)) > 0) {
                mayor = izquierdo;
            }
            if (derecho < tamano && comparador.comparar(obtener(derecho), obtener(mayor)) > 0) {
                mayor = derecho;
            }
            if (mayor == actual) {
                break;
            }
            intercambiar(actual, mayor);
            actual = mayor;
        }
    }

    private void intercambiar(int primero, int segundo) {
        Object temporal = monticulo[primero];
        monticulo[primero] = monticulo[segundo];
        monticulo[segundo] = temporal;
    }
}
